package com.weng.gulimall.feign.search;

import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.model.list.Goods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/inner/rpc/search")
@FeignClient("service-search")
public interface SearchClient {

    @PostMapping("/goods")
    Result saveGoods(@RequestBody Goods goods);

    @DeleteMapping("/delete/{skuId}")
    Result deleteGoods(@PathVariable("skuId") Long skuId);
}
