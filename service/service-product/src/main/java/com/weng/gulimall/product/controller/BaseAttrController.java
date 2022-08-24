package com.weng.gulimall.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.model.product.BaseAttrInfo;
import com.weng.gulimall.model.product.BaseAttrValue;
import com.weng.gulimall.product.service.BaseAttrInfoService;
import com.weng.gulimall.product.service.BaseAttrValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin/product/")
@RestController
public class BaseAttrController {

    @Autowired
    private BaseAttrInfoService baseAttrInfoService;

    @Autowired
    private BaseAttrValueService baseAttrValueService;


    //通过分类id查询平台属性
    @GetMapping("attrInfoList/{c1id}/{c2id}/{c3id}")
    public Result<List<BaseAttrInfo>> getAttrInfoList(@PathVariable("c1id") Long c1id,
                                                      @PathVariable("c2id") Long c2id,
                                                      @PathVariable("c3id") Long c3id) {

        return Result.ok(baseAttrInfoService.getAttrInfoByCategoryId(c1id, c2id, c3id));
    }

    //    新增属性信息  或者 修改属性信息
    @PostMapping("/saveAttrInfo")
    public Result saveAttrInfo(@RequestBody BaseAttrInfo baseAttrInfo) {
        baseAttrInfoService.saveOrUpdateAttrInfo(baseAttrInfo);
        return Result.ok();
    }

    //根据属性id，获取属性信息回显
    @GetMapping("/getAttrValueList/{attrId}")
    public Result<List<BaseAttrValue>> getAttrValueList(@PathVariable("attrId") Long attrId) {
        return Result.ok(baseAttrValueService.getAttrValueList(attrId));
    }

}
