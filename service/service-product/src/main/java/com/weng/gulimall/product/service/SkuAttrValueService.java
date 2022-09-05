package com.weng.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weng.gulimall.model.list.SearchAttr;
import com.weng.gulimall.model.product.SkuAttrValue;

import java.util.List;

/**
* @author lingzi
* @description 针对表【sku_attr_value(sku平台属性值关联表)】的数据库操作Service
* @createDate 2022-08-23 22:31:29
*/
public interface SkuAttrValueService extends IService<SkuAttrValue> {


    /**
     * 查询当前skuid所有平台属性名和值
     * @param skuId
     * @return
     */
    List<SearchAttr> getSkuAttrNameAndValue(Long skuId);
}
