package com.weng.gulimall.product.controller;

import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.model.product.BaseCategory1;
import com.weng.gulimall.model.product.BaseCategory2;
import com.weng.gulimall.model.product.BaseCategory3;
import com.weng.gulimall.product.service.BaseCategory1Service;
import com.weng.gulimall.product.service.BaseCategory2Service;
import com.weng.gulimall.product.service.BaseCategory3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/product/")
public class CategoryController {

    @Autowired
    private BaseCategory1Service baseCategory1Service;

    @Autowired
    private BaseCategory2Service baseCategory2Service;

    @Autowired
    private BaseCategory3Service baseCategory3Service;

    //查询一级分类
    @GetMapping("getCategory1")
    public Result<List<BaseCategory1>> getCategory1() {
        return Result.ok(baseCategory1Service.list());
    }

    //查询二级分类
    @GetMapping("getCategory2/{c1id}")
    public Result<List<BaseCategory2>> getCategory2(@PathVariable("c1id") Long c1id) {
        return Result.ok(baseCategory2Service.getCategory1Child(c1id));
    }

    //通过二级分类查询三级分类
    @GetMapping("getCategory3/{c2id}")
    public Result<List<BaseCategory3>> getCategory3(@PathVariable("c2id") Long c2id) {
        return Result.ok(baseCategory3Service.getCategory1Child(c2id));
    }
}
