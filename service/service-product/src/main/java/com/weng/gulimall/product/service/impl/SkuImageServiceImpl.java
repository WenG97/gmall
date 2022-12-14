package com.weng.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weng.gulimall.model.product.SkuImage;
import com.weng.gulimall.product.service.SkuImageService;
import com.weng.gulimall.product.mapper.SkuImageMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author lingzi
* @description 针对表【sku_image(库存单元图片表)】的数据库操作Service实现
* @createDate 2022-08-23 22:31:29
*/
@Service
public class SkuImageServiceImpl extends ServiceImpl<SkuImageMapper, SkuImage>
    implements SkuImageService{

    @Override
    public List<SkuImage> getSkuImage(Long skuId) {
        LambdaQueryWrapper<SkuImage> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SkuImage::getSkuId,skuId);
        return list(lambdaQueryWrapper);
    }
}




