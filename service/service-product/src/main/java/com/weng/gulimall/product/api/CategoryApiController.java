package com.weng.gulimall.product.api;

import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.model.to.CategoryTreeTo;
import com.weng.gulimall.product.service.BaseCategory2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/inner/rpc")
public class CategoryApiController {

    @Autowired
    private BaseCategory2Service baseCategory2Service;

    @GetMapping("/category/tree")
    public Result<List<CategoryTreeTo>> getAllCategoryTree(){
        List<CategoryTreeTo> categoryTreeTos = baseCategory2Service.getAllCategoryWithTree();
        return Result.ok(categoryTreeTos);
    }

}
