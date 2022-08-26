package com.weng.gulimall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weng.gulimall.model.product.BaseCategory3;
import com.weng.gulimall.model.to.CategoryViewTo;
import org.apache.ibatis.annotations.Param;

/**
* @author lingzi
* @description 针对表【base_category3(三级分类表)】的数据库操作Mapper
* @createDate 2022-08-23 04:25:08
* @Entity com.weng.gulimall.product.domain.BaseCategory3
*/
public interface BaseCategory3Mapper extends BaseMapper<BaseCategory3> {

    CategoryViewTo getCategoryView(@Param("category3Id") Long category3Id);
}




