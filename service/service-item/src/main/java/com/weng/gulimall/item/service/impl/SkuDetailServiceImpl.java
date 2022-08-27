package com.weng.gulimall.item.service.impl;

import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.item.feign.SkuDetailFeignClient;
import com.weng.gulimall.item.service.SkuDetailService;
import com.weng.gulimall.model.product.SkuImage;
import com.weng.gulimall.model.product.SkuInfo;
import com.weng.gulimall.model.product.SpuSaleAttr;
import com.weng.gulimall.model.to.CategoryViewTo;
import com.weng.gulimall.model.to.SkuDetailTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class SkuDetailServiceImpl implements SkuDetailService {

    @Autowired
    private SkuDetailFeignClient skuDetailFeignClient;

    @Autowired
    private ThreadPoolExecutor executor;

    @Override
    public SkuDetailTo getSkuDetail(Long skuId) {
        //远程调用商品服务查询
        // SkuDetailTo skuDetail = skuDetailFeignClient.getSkuDetail(skuId);

        SkuDetailTo skuDetailTo = new SkuDetailTo();
        //1、查询skuinfo基本信息
        CompletableFuture<SkuInfo> skuInfoFuture =
                CompletableFuture.supplyAsync(() -> {
                    Result<SkuInfo> result =
                            skuDetailFeignClient.getSkuInfo(skuId);
                    SkuInfo skuInfo = result.getData();
                    skuDetailTo.setSkuInfo(skuInfo);
                    return skuInfo;
                }, executor);


        //2、查询图片基本信息
        CompletableFuture<Void> imagesFuture =
                skuInfoFuture.thenAcceptAsync(skuInfo -> {
                    Result<List<SkuImage>> skuImages =
                            skuDetailFeignClient.getSkuImages(skuId);
                    skuInfo.setSkuImageList(skuImages.getData());
                }, executor);


        //3、查询商品实时价格
        CompletableFuture<Void> priceFuture =
                CompletableFuture.runAsync(() -> {
                    Result<BigDecimal> sku1010Price =
                            skuDetailFeignClient.getSku1010Price(skuId);
                    skuDetailTo.setPrice(sku1010Price.getData());
                }, executor);


        //4、查询销售属性名值组合
        CompletableFuture<Void> saleAttrFuture =
                skuInfoFuture.thenAcceptAsync(skuInfo -> {
                    Result<List<SpuSaleAttr>> skuSaleAttrValues =
                            skuDetailFeignClient.getSkuSaleAttrValues(skuId, skuInfo.getSpuId());
                    skuDetailTo.setSpuSaleAttrList(skuSaleAttrValues.getData());
                }, executor);

        //5、查询sku兄弟组合
        CompletableFuture<Void> skuValueFuture =
                skuInfoFuture.thenAcceptAsync(skuInfo -> {
                    Result<String> skuValueJson =
                            skuDetailFeignClient.getSkuValueJson(skuInfo.getSpuId());
                    skuDetailTo.setValueSkuJson(skuValueJson.getData());
                }, executor);

        //6、查询分类类目
        CompletableFuture<Void> categoryViewFuture =
                skuInfoFuture.thenAcceptAsync(skuInfo -> {
                    Result<CategoryViewTo> categoryView =
                            skuDetailFeignClient.getCategoryView(skuInfo.getCategory3Id());
                    skuDetailTo.setCategoryView(categoryView.getData());
                }, executor);

        CompletableFuture.allOf(imagesFuture, priceFuture, saleAttrFuture, skuValueFuture, categoryViewFuture)
                .join();
        return skuDetailTo;
    }
}
