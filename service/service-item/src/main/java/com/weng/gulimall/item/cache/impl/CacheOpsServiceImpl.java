package com.weng.gulimall.item.cache.impl;

import com.weng.gulimall.common.constant.SysRedisConst;
import com.weng.gulimall.common.util.Jsons;
import com.weng.gulimall.item.cache.CacheOpsService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * 查询缓存
 */
@Service
public class CacheOpsServiceImpl implements CacheOpsService {

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 从缓存中获取数据,并且转成对应的类型
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
    public boolean bloomFilter(Long cacheKey) {

        return true;
    }

    @Override
    public boolean tryLock(Long skuId) {
        String lockKey = SysRedisConst.LOCK_SKU_DETAIL + skuId;
        RLock lock = redissonClient.getLock(lockKey);
        return lock.tryLock();
    }

    @Override
    public void saveData(String cacheKey, Object obj) {
        if (obj == null){
        stringRedisTemplate.opsForValue().set(cacheKey,SysRedisConst.NULL_VAL,SysRedisConst.NULL_VAL_TTL, TimeUnit.SECONDS);
        }else {
        stringRedisTemplate.opsForValue().set(cacheKey,Jsons.toStr(obj),SysRedisConst.SKUDETAIL_VAL_TTL,TimeUnit.SECONDS);
        }
    }

    @Override
    public void unlock(Long skuId) {
        String lockKey = SysRedisConst.LOCK_SKU_DETAIL + skuId;
        RLock lock = redissonClient.getLock(lockKey);
        lock.unlock();
    }
}
