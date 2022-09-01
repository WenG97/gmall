package com.weng.gulimall.product.controller;

import com.weng.gulimall.common.constant.SysRedisConst;
import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.product.bloom.BloomOpsService;
import com.weng.gulimall.product.bloom.BloomQueryDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin/product")
@RestController
public class BloomOpsController {


    @Autowired
    private BloomOpsService bloomOpsService;
    @Autowired
    private BloomQueryDataService bloomQueryDataService;

    @GetMapping("/rebuild/sku/now")
    public Result rebuildBloom(){
        String bloomSkuid = SysRedisConst.BLOOM_SKUID;
        bloomOpsService.rebuildBloom(bloomSkuid,bloomQueryDataService);
        return Result.ok();
    }
}
