package com.weng.gulimall.product.api;

import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.model.product.SkuImage;
import com.weng.gulimall.model.product.SkuInfo;
import com.weng.gulimall.model.product.SpuSaleAttr;
import com.weng.gulimall.model.to.CategoryViewTo;
import com.weng.gulimall.product.service.BaseCategory3Service;
import com.weng.gulimall.product.service.SkuInfoService;
import com.weng.gulimall.product.service.SpuSaleAttrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/inner/rpc/product")
public class SkuDetailApiController {

    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private SpuSaleAttrService spuSaleAttrService;

    @Autowired
    private BaseCategory3Service baseCategory3Service;
    // @GetMapping("/skuDetail/{skuId}")
    // public SkuDetailTo getSkuDetail(@PathVariable("skuId") Long skuId) {
    //     SkuDetailTo skuDetail = skuInfoService.getSkuDetail(skuId);
    //     return skuDetail;
    // }

    //查询skuInfo的基本信息
    @GetMapping("/skuDetail/info/{skuId}")
    public Result<SkuInfo> getSkuInfo(@PathVariable("skuId") Long skuId) {
        SkuInfo skuInfo = skuInfoService.getDetailSkuInfo(skuId);
        return Result.ok(skuInfo);
    }

    //查询skuInfo的图片信息
    @GetMapping("/skuDetail/images/{skuId}")
    public Result<List<SkuImage>> getSkuImages(@PathVariable("skuId") Long skuId) {
        List<SkuImage> images = skuInfoService.getDetailSkuImages(skuId);
        return Result.ok(images);
    }

    //查询sku的实时价格
    @GetMapping("/skuDetail/price/{skuId}")
    public Result<BigDecimal> getSku1010Price(@PathVariable("skuId") Long skuId) {
        BigDecimal price = skuInfoService.get1010Price(skuId);
        return Result.ok(price);
    }

    //查询sku的销售属性名和值,并且标记
    @GetMapping("/skuDetail/saleAttrValues/{skuId}/{spuId}")
    public Result<List<SpuSaleAttr>> getSkuSaleAttrValues(@PathVariable("skuId") Long skuId,
                                                          @PathVariable("spuId") Long spuId) {
        List<SpuSaleAttr> saleAttrAndValueMarkSku =
                spuSaleAttrService.getSaleAttrAndValueMarkSku(spuId, skuId);

        return Result.ok(saleAttrAndValueMarkSku);
    }

    //查询sku的兄弟sku
    @GetMapping("/skuDetail/valueJson/{spuId}")
    public Result<String> getSkuValueJson(@PathVariable("spuId") Long spuId) {
        String  jsonTos =
                spuSaleAttrService.getAllSkuSaleAttrValueJson(spuId);

        return Result.ok(jsonTos);
    }

    //查询sku的分类
    @GetMapping("/skuDetail/categoryView/{c3Id}")
    public Result<CategoryViewTo> getCategoryView(@PathVariable("c3Id") Long c3Id) {
        CategoryViewTo categoryViewTo = baseCategory3Service.getCategoryView(c3Id);
        return Result.ok(categoryViewTo);
    }

}
