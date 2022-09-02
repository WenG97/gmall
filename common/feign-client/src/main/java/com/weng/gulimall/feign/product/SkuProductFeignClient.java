package com.weng.gulimall.feign.product;

import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.model.product.SkuImage;
import com.weng.gulimall.model.product.SkuInfo;
import com.weng.gulimall.model.product.SpuSaleAttr;
import com.weng.gulimall.model.to.CategoryViewTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

@RequestMapping("/api/inner/rpc/product")
@FeignClient("service-product")
public interface SkuProductFeignClient {

    // @GetMapping("/skuDetail/{skuId}")
    // SkuDetailTo getSkuDetail(@PathVariable("skuId") Long skuId);

    //查询skuInfo的基本信息
    @GetMapping("/skuDetail/info/{skuId}")
    Result<SkuInfo> getSkuInfo(@PathVariable("skuId") Long skuId);

    //查询skuInfo的图片信息
    @GetMapping("/skuDetail/images/{skuId}")
    Result<List<SkuImage>> getSkuImages(@PathVariable("skuId") Long skuId);


    //查询sku的实时价格
    @GetMapping("/skuDetail/price/{skuId}")
    Result<BigDecimal> getSku1010Price(@PathVariable("skuId") Long skuId);

    //查询sku的销售属性名和值,并且标记
    @GetMapping("/skuDetail/saleAttrValues/{skuId}/{spuId}")
    Result<List<SpuSaleAttr>> getSkuSaleAttrValues(@PathVariable("skuId") Long skuId,
                                                   @PathVariable("spuId") Long spuId);

    //查询sku的兄弟sku
    @GetMapping("/skuDetail/valueJson/{spuId}")
    Result<String> getSkuValueJson(@PathVariable("spuId") Long spuId);


    //查询sku的分类
    @GetMapping("/skuDetail/categoryView/{c3Id}")
    Result<CategoryViewTo> getCategoryView(@PathVariable("c3Id") Long c3Id);

}
