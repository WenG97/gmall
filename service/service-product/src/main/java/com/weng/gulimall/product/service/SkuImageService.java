package com.weng.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weng.gulimall.model.product.SkuImage;

import java.util.List;

/**
* @author lingzi
* @description 针对表【sku_image(库存单元图片表)】的数据库操作Service
* @createDate 2022-08-23 22:31:29
*/
public interface SkuImageService extends IService<SkuImage> {

    /**
     * 查询某个sku的图片
     * @param skuId
     * @return
     */

    List<SkuImage> getSkuImage(Long skuId);
}
