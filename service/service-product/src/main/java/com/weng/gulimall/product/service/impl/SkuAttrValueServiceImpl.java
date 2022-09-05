package com.weng.gulimall.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weng.gulimall.model.list.SearchAttr;
import com.weng.gulimall.model.product.SkuAttrValue;
import com.weng.gulimall.product.service.SkuAttrValueService;
import com.weng.gulimall.product.mapper.SkuAttrValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author lingzi
* @description 针对表【sku_attr_value(sku平台属性值关联表)】的数据库操作Service实现
* @createDate 2022-08-23 22:31:29
*/
@Service
public class SkuAttrValueServiceImpl extends ServiceImpl<SkuAttrValueMapper, SkuAttrValue>
    implements SkuAttrValueService{

    @Autowired
    private SkuAttrValueMapper skuAttrValueMapper;

    @Override
    public List<SearchAttr> getSkuAttrNameAndValue(Long skuId) {

        return skuAttrValueMapper.getSkuAttrNameAndValue(skuId);
    }
}




