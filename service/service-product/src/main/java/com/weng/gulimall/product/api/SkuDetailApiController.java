package com.weng.gulimall.product.api;

import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.model.to.SkuDetailTo;
import com.weng.gulimall.product.service.SkuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inner/rpc/product")
public class SkuDetailApiController {

    @Autowired
    private SkuInfoService skuInfoService;

    @GetMapping("/skuDetail/{skuId}")
    public Result<SkuDetailTo> getSkuDetail(@PathVariable("skuId") Long skuId){
       SkuDetailTo skuDetail = skuInfoService.getSkuDetail(skuId);

        return Result.ok(skuDetail);
    }

}
