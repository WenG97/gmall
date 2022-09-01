package com.weng.gulimall.product.bloom.impl;

import com.weng.gulimall.product.bloom.BloomOpsService;
import com.weng.gulimall.product.bloom.BloomQueryDataService;
import com.weng.gulimall.product.service.SkuInfoService;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 重建指定的BloomFilter
 */
@Service
public class BloomOpsServiceImpl implements BloomOpsService {

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public void rebuildBloom(String bloomName, BloomQueryDataService bloomQueryDataService) {
        //获取到当前的布隆过滤器
        RBloomFilter<Object> oldBloomFilter = redissonClient.getBloomFilter(bloomName);

        //创建一个新的布隆过滤器替代旧的
        RBloomFilter<Object> newBloom = redissonClient.getBloomFilter(bloomName + "_new");
        //初始化新的布隆
        newBloom.tryInit(5000000, 0.000001);

        for (Object aLong : bloomQueryDataService.queryData()) {
            newBloom.add(aLong);
        }
        //新旧交换,最好的做法还是lua脚本
        oldBloomFilter.rename(bloomName + "_temp");
        newBloom.rename(bloomName);
        //删除老布隆
        oldBloomFilter.delete();
    }
}
