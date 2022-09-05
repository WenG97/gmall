package com.weng.gulimall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weng.gulimall.model.list.SearchAttr;
import com.weng.gulimall.model.product.SkuAttrValue;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author lingzi
* @description 针对表【sku_attr_value(sku平台属性值关联表)】的数据库操作Mapper
* @createDate 2022-08-23 22:31:29
* @Entity com.weng.gulimall.product.domain.SkuAttrValue
*/
public interface SkuAttrValueMapper extends BaseMapper<SkuAttrValue> {

    List<SearchAttr> getSkuAttrNameAndValue(@Param("skuId") Long skuId);
}




