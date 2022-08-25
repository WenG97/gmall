package com.weng.gulimall.product.controller;

import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.model.product.BaseSaleAttr;
import com.weng.gulimall.model.product.SpuInfo;
import com.weng.gulimall.product.service.BaseSaleAttrService;
import com.weng.gulimall.product.service.SpuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/product/")
public class BaseSaleAttrController {

    @Autowired
    private BaseSaleAttrService baseSaleAttrService;

    @Autowired
    private SpuInfoService spuInfoService;

    @GetMapping("baseSaleAttrList")
    public Result baseSaleAttr(){
        return Result.ok(baseSaleAttrService.list());
    }



}
