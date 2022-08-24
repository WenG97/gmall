package com.weng.gulimall.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weng.gulimall.model.product.SkuInfo;
import com.weng.gulimall.product.service.SkuInfoService;
import com.weng.gulimall.product.mapper.SkuInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author lingzi
* @description 针对表【sku_info(库存单元表)】的数据库操作Service实现
* @createDate 2022-08-23 22:31:29
*/
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo>
    implements SkuInfoService{

}




