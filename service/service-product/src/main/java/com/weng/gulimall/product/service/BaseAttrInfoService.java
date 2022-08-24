package com.weng.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weng.gulimall.model.product.BaseAttrInfo;

import java.util.List;

/**
* @author lingzi
* @description 针对表【base_attr_info(属性表)】的数据库操作Service
* @createDate 2022-08-23 22:31:29
*/
public interface BaseAttrInfoService extends IService<BaseAttrInfo> {

    List<BaseAttrInfo> getAttrInfoByCategoryId(Long c1id, Long c2id, Long c3id);

    void saveOrUpdateAttrInfo(BaseAttrInfo baseAttrInfo);
}
