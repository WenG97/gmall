package com.weng.gulimall.product.bloom.impl;

import com.weng.gulimall.product.bloom.BloomQueryDataService;
import com.weng.gulimall.product.service.SkuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkuBloomQueryDataServiceImpl implements BloomQueryDataService {

    @Autowired
    private SkuInfoService skuInfoService;

    @Override
    public List<Long> queryData() {
        return skuInfoService.findAllSkuId();
    }
}
