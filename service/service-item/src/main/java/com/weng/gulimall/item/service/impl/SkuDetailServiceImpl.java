package com.weng.gulimall.item.service.impl;

import com.weng.gulimall.common.constant.SysRedisConst;
import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.item.feign.SkuDetailFeignClient;
import com.weng.gulimall.item.service.SkuDetailService;
import com.weng.gulimall.model.product.SkuImage;
import com.weng.gulimall.model.product.SkuInfo;
import com.weng.gulimall.model.product.SpuSaleAttr;
import com.weng.gulimall.model.to.CategoryViewTo;
import com.weng.gulimall.model.to.SkuDetailTo;
import com.weng.starter.cache.service.CacheOpsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class SkuDetailServiceImpl implements SkuDetailService {

    @Autowired
    private SkuDetailFeignClient skuDetailFeignClient;

    @Autowired
    private ThreadPoolExecutor executor;

    @Autowired
    private CacheOpsService cacheOpsService;


    /**
     * 远程调用dao层，查询数据库数据
     *
     * @param skuId
     * @return
     */
    public SkuDetailTo getSkuDetailRemote(Long skuId) {
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
                    if (skuInfo==null){
                        return;
                    }
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
                    if (skuInfo==null){
                        return;
                    }
                    Result<List<SpuSaleAttr>> skuSaleAttrValues =
                            skuDetailFeignClient.getSkuSaleAttrValues(skuId, skuInfo.getSpuId());
                    skuDetailTo.setSpuSaleAttrList(skuSaleAttrValues.getData());
                }, executor);

        //5、查询sku兄弟组合
        CompletableFuture<Void> skuValueFuture =
                skuInfoFuture.thenAcceptAsync(skuInfo -> {
                    if (skuInfo==null){
                        return;
                    }
                    Result<String> skuValueJson =
                            skuDetailFeignClient.getSkuValueJson(skuInfo.getSpuId());
                    skuDetailTo.setValueSkuJson(skuValueJson.getData());
                }, executor);

        //6、查询分类类目
        CompletableFuture<Void> categoryViewFuture =
                skuInfoFuture.thenAcceptAsync(skuInfo -> {
                    if (skuInfo==null){
                        return;
                    }
                    Result<CategoryViewTo> categoryView =
                            skuDetailFeignClient.getCategoryView(skuInfo.getCategory3Id());
                    skuDetailTo.setCategoryView(categoryView.getData());
                }, executor);

        CompletableFuture.allOf(imagesFuture, priceFuture, saleAttrFuture, skuValueFuture, categoryViewFuture)
                .join();
        return skuDetailTo;
    }

    /**
     * 请求进来首先查询缓存层
     *
     * @param skuId
     * @return
     */
    @Override
    public SkuDetailTo getSkuDetail(Long skuId) {
        //1、请求进来先查询缓存
        String cacheKey = SysRedisConst.SKU_INFO_PREFIX + skuId;
        SkuDetailTo cacheData = cacheOpsService.getCacheData(cacheKey, SkuDetailTo.class);
        //2、判断缓存中，有无数据
        if (cacheData == null) {
            //3、如果缓存中没有，查询布隆过滤器,
            //4、判断布隆过滤器中有没有当前商品
            if (!cacheOpsService.bloomContains(skuId)) {
                //5、如果布隆过滤器说数据库中没有当前数据，说明数据库确实没有，直接返回空
                return null;
            }else {
                //6、如果布隆过滤器说数据库有数据，则数据库可能有,加锁查询数据库
                if (cacheOpsService.tryLock(skuId)){
                    //拿到锁了,远程查询数据库
                    SkuDetailTo skuDetailRemote = getSkuDetailRemote(skuId);
                    if (skuDetailRemote.getSkuInfo()==null){
                        skuDetailRemote=null;
                    }
                    //8、添加缓存,然后返回
                    cacheOpsService.saveData(cacheKey,skuDetailRemote);
                    cacheOpsService.unlock(skuId);
                    return skuDetailRemote;
                }
                //没有拿到锁,睡一秒，直查缓存
                try {
                    TimeUnit.SECONDS.sleep(1);
                    return cacheOpsService.getCacheData(cacheKey,SkuDetailTo.class);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        //2、如果缓存中有数据，则直接返回
        return cacheData;
    }

}
