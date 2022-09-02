package com.weng.gulimall.item.api;

import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.item.service.SkuDetailService;
import com.weng.gulimall.model.to.SkuDetailTo;
import feign.Retryer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/inner/rpc/item")
@RestController
public class SkuDetailApiController {

    @Autowired
    private SkuDetailService skuDetailService;

    @GetMapping("/skuDetail/{skuId}")
    public Result<SkuDetailTo> getSkuDetail(@PathVariable("skuId") Long skuId){
        SkuDetailTo skuDetailTo =skuDetailService.getSkuDetail(skuId);
        return Result.ok(skuDetailTo);
    }
}
