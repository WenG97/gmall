package com.weng.gulimall.product.controller;

import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.model.product.BaseCategory1;
import com.weng.gulimall.model.product.BaseCategory2;
import com.weng.gulimall.product.service.BaseCategory1Service;
import com.weng.gulimall.product.service.BaseCategory2Service;
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
    //查询一级分类
    @GetMapping("getCategory1")
    public Result<List<BaseCategory1>> getCategory1(){
        return Result.ok(baseCategory1Service.list());
    }

//查询二级分类
    @GetMapping("getCategory2/{c1id}")
    public Result<List<BaseCategory2>> getCategory2(@PathVariable("c1id") Long id){
        List<BaseCategory2> list =  baseCategory2Service.getCategory1Child(id);
        return Result.ok(list);
    }
}
