package com.weng.gulimall.item.service.impl;

import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.item.feign.SkuDetailFeignClient;
import com.weng.gulimall.item.service.SkuDetailService;
import com.weng.gulimall.model.to.SkuDetailTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SkuDetailServiceImpl implements SkuDetailService {

    @Autowired
    private SkuDetailFeignClient skuDetailFeignClient;

    @Override
    public SkuDetailTo getSkuDetail(Long skuId) {
        Result<SkuDetailTo> skuDetail = skuDetailFeignClient.getSkuDetail(skuId);


        return skuDetail.getData();
    }
}
