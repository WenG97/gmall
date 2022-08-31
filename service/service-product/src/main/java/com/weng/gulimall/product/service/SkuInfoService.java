package com.weng.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weng.gulimall.model.product.SkuImage;
import com.weng.gulimall.model.product.SkuInfo;
import com.weng.gulimall.model.to.SkuDetailTo;

import java.math.BigDecimal;
import java.util.List;

/**
* @author lingzi
* @description 针对表【sku_info(库存单元表)】的数据库操作Service
* @createDate 2022-08-23 22:31:29
*/
public interface SkuInfoService extends IService<SkuInfo> {

    void savaSkuInfo(SkuInfo skuInfo);


    void updateSaleStatus(Long skuId, Integer i);

    SkuDetailTo getSkuDetail(Long skuId);

     BigDecimal get1010Price(Long skuId);

    SkuInfo getDetailSkuInfo(Long skuId);

    List<SkuImage> getDetailSkuImages(Long skuId);

    List<Long> findAllSkuId();
}
