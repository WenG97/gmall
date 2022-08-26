package com.weng.gulimall.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.model.product.SkuInfo;
import com.weng.gulimall.model.product.SpuImage;
import com.weng.gulimall.model.product.SpuSaleAttr;
import com.weng.gulimall.product.service.SkuInfoService;
import com.weng.gulimall.product.service.SpuImageService;
import com.weng.gulimall.product.service.SpuSaleAttrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/product/")
public class SkuController {

    @Autowired
    private SkuInfoService skuInfoService;
    @Autowired
    private SpuImageService spuImageService;
    @Autowired
    private SpuSaleAttrService spuSaleAttrService;

    @GetMapping("list/{pn}/{ps}")
    public Result<Page<SkuInfo>> getSkuList(@PathVariable("pn") Long pn,
                             @PathVariable("ps") Long ps){
        Page<SkuInfo> page = skuInfoService.page(new Page<SkuInfo>(pn, ps));
        return Result.ok(page);
    }

    @GetMapping("spuImageList/{spuId}")
    public Result<List<SpuImage>> getImageList(@PathVariable("spuId") Long spuId){
        LambdaQueryWrapper<SpuImage> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SpuImage::getSpuId,spuId);
        List<SpuImage> list = spuImageService.list(lambdaQueryWrapper);
        return Result.ok(list);
    }

    @GetMapping("spuSaleAttrList/{spuId}")
    public Result<List<SpuSaleAttr>> spuSaleAttrList(@PathVariable("spuId")Long spuId){
        List<SpuSaleAttr> list = spuSaleAttrService.getSaleAndAttrBySpuId(spuId);
        return Result.ok(list);
    }

    @PostMapping("savaSkuInfo")
    public Result savaSkuInfo(@RequestBody SkuInfo skuInfo){
        skuInfoService.savaSkuInfo(skuInfo);
        return Result.ok();
    }

    @GetMapping("cancelSale/{skuId}")
    public Result cancelSale(@PathVariable("skuId") Long skuId){
        skuInfoService.updateSaleStatus(skuId,0);
        return Result.ok();
    }

    @GetMapping("onSale/{skuId}")
    public Result onSale(@PathVariable("skuId") Long skuId){
        skuInfoService.updateSaleStatus(skuId,1);
        return Result.ok();
    }
}
