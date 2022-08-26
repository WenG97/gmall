package com.weng.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weng.gulimall.model.product.SpuSaleAttr;

import java.util.List;

/**
* @author lingzi
* @description 针对表【spu_sale_attr(spu销售属性)】的数据库操作Service
* @createDate 2022-08-23 22:31:29
*/
public interface SpuSaleAttrService extends IService<SpuSaleAttr> {

    List<SpuSaleAttr> getSaleAndAttrBySpuId(Long spuId);

    List<SpuSaleAttr> getSaleAttrAndValueMarkSku(Long spuId, Long skuId);
}
