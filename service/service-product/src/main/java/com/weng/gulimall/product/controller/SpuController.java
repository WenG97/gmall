package com.weng.gulimall.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.model.product.SpuInfo;
import com.weng.gulimall.product.service.SpuInfoService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "SPU业务")
@RestController
@RequestMapping("/admin/product/")
public class SpuController {

    @Autowired
    private SpuInfoService spuInfoService;

    @GetMapping("{pn}/{ps}")
    public Result<Page<SpuInfo>> getSupPage(@PathVariable("pn") Long pn,
                             @PathVariable("ps") Long ps,
                             @RequestParam("category3Id") Long category3Id) {
        LambdaQueryWrapper<SpuInfo> lambda = new LambdaQueryWrapper<SpuInfo>().eq(SpuInfo::getCategory3Id, category3Id);
        Page<SpuInfo> page = spuInfoService.page(new Page<>(pn, ps), lambda);
        return Result.ok(page);
    }

    @PostMapping("saveSpuInfo")
    public Result saveSpuInfo(@RequestBody SpuInfo spuInfo){
        spuInfoService.saveSpuInfo(spuInfo);
        return Result.ok();
    }
}
