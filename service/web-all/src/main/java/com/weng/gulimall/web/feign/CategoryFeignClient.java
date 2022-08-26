package com.weng.gulimall.web.feign;

import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.model.to.CategoryTreeTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/inner/rpc")
@FeignClient("service-product")
public interface CategoryFeignClient {


    @GetMapping("/category/tree")
    Result<List<CategoryTreeTo>> getAllCategoryTree();
}
