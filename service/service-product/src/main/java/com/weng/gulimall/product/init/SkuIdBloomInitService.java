package com.weng.gulimall.product.init;

import com.weng.gulimall.common.constant.SysRedisConst;
import com.weng.gulimall.product.service.SkuInfoService;
import org.redisson.api.ObjectListener;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class SkuIdBloomInitService {

    @Autowired
    private SkuInfoService skuInfoService;
    @Autowired
    private RedissonClient redissonClient;

    @PostConstruct
    public void initSkuBloom(){
        //1、查询所有id
        List<Long> skuIds = skuInfoService.findAllSkuId();
        //2、把所有id初始化到布隆过滤器
        RBloomFilter<Object> bloomFilter =
                redissonClient.getBloomFilter(SysRedisConst.BLOOM_SKUID);
        if (!bloomFilter.isExists()){
        bloomFilter.tryInit(5000000,0.000001);
        }
        for (Long skuId : skuIds) {
            bloomFilter.add(skuId);
        }
    }
}
