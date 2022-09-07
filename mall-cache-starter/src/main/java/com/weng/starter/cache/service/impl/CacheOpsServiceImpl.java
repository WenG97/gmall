package com.weng.starter.cache.service.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import com.weng.starter.cache.constant.SysRedisConst;
import com.weng.starter.cache.service.CacheOpsService;
import com.weng.starter.cache.utils.Jsons;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Type;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 查询缓存
 */
@Service
public class CacheOpsServiceImpl implements CacheOpsService {

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    //todo:可以封装在配置文件中
    private final ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(4);


    /**
     * 从缓存中获取数据,并且转成对应的类型
     *
     * @param cacheKey
     * @param <T>
     * @return
     */
    @Override
    public <T> T getCacheData(String cacheKey, Class<T> clz) {
        String s = stringRedisTemplate.opsForValue().get(cacheKey);
        if (SysRedisConst.NULL_VAL.equals(s) || StringUtils.isEmpty(s)) {
            return null;
        }
        return Jsons.toObj(s, clz);
    }

    @Override
    public Object getCacheData(String cacheKey, Type type) {
        String s = stringRedisTemplate.opsForValue().get(cacheKey);
        if (SysRedisConst.NULL_VAL.equals(s) || StringUtils.isEmpty(s)) {
            return null;
        }
        return Jsons.toObj(s, new TypeReference<Object>() {
            @Override
            public Type getType() {
                return type;
            }
        });
    }

    @Override
    public boolean bloomContains(Long skuId) {
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter(SysRedisConst.BLOOM_SKUID);
        if (bloomFilter.isExists()) {
            return bloomFilter.contains(skuId);
        }
        return false;
    }

    @Override
    public boolean bloomContains(String bloomName, Object bVal) {
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter(bloomName);
        return bloomFilter.contains(bVal);
    }

    @Override
    public boolean tryLock(Long skuId) {
        String lockKey = SysRedisConst.LOCK_SKU_DETAIL + skuId;
        RLock lock = redissonClient.getLock(lockKey);
        return lock.tryLock();
    }

    @Override
    public boolean tryLock(String lockName) {
        RLock lock = redissonClient.getLock(lockName);
        return lock.tryLock();
    }

    @Override
    public void saveData(String cacheKey, Object obj) {
        if (obj == null) {
            stringRedisTemplate.opsForValue().set(cacheKey, SysRedisConst.NULL_VAL, SysRedisConst.NULL_VAL_TTL, TimeUnit.SECONDS);
        } else {
            stringRedisTemplate.opsForValue().set(cacheKey, Jsons.toStr(obj), SysRedisConst.SKUDETAIL_VAL_TTL, TimeUnit.SECONDS);
        }
    }

    public void saveData(String cacheKey, Object obj, Long dataTtl) {
        if (obj == null) {
            stringRedisTemplate.opsForValue().set(cacheKey, SysRedisConst.NULL_VAL, SysRedisConst.NULL_VAL_TTL, TimeUnit.SECONDS);
        } else {
            stringRedisTemplate.opsForValue().set(cacheKey, Jsons.toStr(obj), dataTtl, TimeUnit.SECONDS);
        }
    }


    @Override
    public void unlock(Long skuId) {
        String lockKey = SysRedisConst.LOCK_SKU_DETAIL + skuId;
        RLock lock = redissonClient.getLock(lockKey);
        lock.unlock();
    }

    @Override
    public void unlock(String lockName) {
        RLock lock = redissonClient.getLock(lockName);
        lock.unlock();
    }

    /**
     * 延迟双删
     *
     * @param cacheKey
     */
    @Override
    public void delay2Delete(String cacheKey) {
        stringRedisTemplate.delete(cacheKey);

        //todo:可以使用redisson自带的任务调用api
        scheduledExecutor.schedule(() -> {
            stringRedisTemplate.delete(cacheKey);
        },10,TimeUnit.SECONDS);
    }
}
