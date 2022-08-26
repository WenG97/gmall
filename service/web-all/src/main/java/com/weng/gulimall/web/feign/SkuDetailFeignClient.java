package com.weng.gulimall.web.feign;

import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.model.to.SkuDetailTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/inner/rpc/item")
@FeignClient("service-item")
public interface SkuDetailFeignClient {

    @GetMapping("/skuDetail/{skuId}")
    Result<SkuDetailTo> getSkuDetail(@PathVariable("skuId") Long skuId);

}
