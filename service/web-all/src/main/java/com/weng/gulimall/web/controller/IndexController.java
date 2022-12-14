package com.weng.gulimall.web.controller;

import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.feign.product.CategoryFeignClient;
import com.weng.gulimall.model.to.CategoryTreeTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private CategoryFeignClient categoryFeignClient;

    @GetMapping({"/", "/index","/index.html"})
    public String indexPage(Model model){
        Result<List<CategoryTreeTo>> result = categoryFeignClient.getAllCategoryTree();
        if (result.isOk()){
        model.addAttribute("list",result.getData());
        }
        return "index/index";
    }
}
