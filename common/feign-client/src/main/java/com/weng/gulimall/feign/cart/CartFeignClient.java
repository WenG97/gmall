package com.weng.gulimall.feign.cart;

import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.model.product.SkuInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api/inner/rpc/cart")
@FeignClient("service-cart")
public interface CartFeignClient {

    @GetMapping("/addToCart")
    Result<SkuInfo> addToCart(@RequestParam("skuId")Long skuId, @RequestParam("num")Integer num);

    @GetMapping("/deleteChecked")
    Result deleteChecked();
}
