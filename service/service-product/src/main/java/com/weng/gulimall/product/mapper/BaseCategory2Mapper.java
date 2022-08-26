package com.weng.gulimall.product.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weng.gulimall.model.product.BaseCategory2;
import com.weng.gulimall.model.to.CategoryTreeTo;

import java.util.List;

/**
* @author lingzi
* @description 针对表【base_category2(二级分类表)】的数据库操作Mapper
* @createDate 2022-08-23 04:25:08
* @Entity com.weng.gulimall.product.domain.BaseCategory2
*/
public interface BaseCategory2Mapper extends BaseMapper<BaseCategory2> {

    List<CategoryTreeTo> getAllCategoryWithTree();
}




