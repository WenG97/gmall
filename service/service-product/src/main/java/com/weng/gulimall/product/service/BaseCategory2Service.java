package com.weng.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weng.gulimall.model.product.BaseCategory2;

import java.util.List;

/**
* @author lingzi
* @description 针对表【base_category2(二级分类表)】的数据库操作Service
* @createDate 2022-08-23 04:25:08
*/
public interface BaseCategory2Service extends IService<BaseCategory2> {

    List<BaseCategory2> getCategory1Child(Long id);
}
