package com.weng.gulimall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weng.gulimall.model.product.BaseAttrInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author lingzi
* @description 针对表【base_attr_info(属性表)】的数据库操作Mapper
* @createDate 2022-08-23 22:31:29
* @Entity com.weng.gulimall.product.domain.BaseAttrInfo
*/
public interface BaseAttrInfoMapper extends BaseMapper<BaseAttrInfo> {

    List<BaseAttrInfo> getAttrInfoByCategoryId(@Param("c1id") Long c1id, @Param("c2id") Long c2id, @Param("c3id") Long c3id);
}




