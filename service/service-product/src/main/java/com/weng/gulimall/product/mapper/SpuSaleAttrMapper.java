package com.weng.gulimall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weng.gulimall.model.product.SpuSaleAttr;

import java.util.List;

/**
* @author lingzi
* @description 针对表【spu_sale_attr(spu销售属性)】的数据库操作Mapper
* @createDate 2022-08-23 22:31:29
* @Entity com.weng.gulimall.product.domain.SpuSaleAttr
*/
public interface SpuSaleAttrMapper extends BaseMapper<SpuSaleAttr> {

    List<SpuSaleAttr> getSaleAndAttrBySpuId(Long spuId);
}




