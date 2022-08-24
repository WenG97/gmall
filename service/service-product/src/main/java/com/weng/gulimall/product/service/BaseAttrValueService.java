package com.weng.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weng.gulimall.model.product.BaseAttrValue;

import java.util.List;

/**
* @author lingzi
* @description 针对表【base_attr_value(属性值表)】的数据库操作Service
* @createDate 2022-08-23 22:31:29
*/
public interface BaseAttrValueService extends IService<BaseAttrValue> {

    List<BaseAttrValue> getAttrValueList(Long attrId);
}
