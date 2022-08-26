package com.weng.gulimall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weng.gulimall.model.product.SkuInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
* @author lingzi
* @description 针对表【sku_info(库存单元表)】的数据库操作Mapper
* @createDate 2022-08-23 22:31:29
* @Entity com.weng.gulimall.product.domain.SkuInfo
*/
public interface SkuInfoMapper extends BaseMapper<SkuInfo> {

    void updateSaleState(@Param("skuId") Long skuId, @Param("state") Integer state);

    BigDecimal getPrice(@Param("skuId") Long skuId);

}




