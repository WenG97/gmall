package com.weng.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weng.gulimall.model.product.BaseCategory2;
import com.weng.gulimall.model.product.BaseCategory3;
import com.weng.gulimall.model.to.CategoryViewTo;

import java.util.List;

/**
* @author lingzi
* @description 针对表【base_category3(三级分类表)】的数据库操作Service
* @createDate 2022-08-23 04:25:08
*/
public interface BaseCategory3Service extends IService<BaseCategory3> {

    List<BaseCategory3> getCategory1Child(Long c2id);

    CategoryViewTo getCategoryView(Long c3Id);
}
