package com.weng.gulimall.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weng.gulimall.common.util.Jsons;
import com.weng.gulimall.model.product.SpuSaleAttr;
import com.weng.gulimall.model.to.ValueSkuJsonTo;
import com.weng.gulimall.product.service.SpuSaleAttrService;
import com.weng.gulimall.product.mapper.SpuSaleAttrMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author lingzi
* @description 针对表【spu_sale_attr(spu销售属性)】的数据库操作Service实现
* @createDate 2022-08-23 22:31:29
*/
@Service
public class SpuSaleAttrServiceImpl extends ServiceImpl<SpuSaleAttrMapper, SpuSaleAttr>
    implements SpuSaleAttrService{

    @Override
    public List<SpuSaleAttr> getSaleAndAttrBySpuId(Long spuId) {
        return baseMapper.getSaleAndAttrBySpuId(spuId);
    }

    @Override
    public List<SpuSaleAttr> getSaleAttrAndValueMarkSku(Long spuId, Long skuId) {

        return baseMapper.getSaleAttrAndValueMarkSku(spuId,skuId);
    }

    @Override
    public String getAllSkuSaleAttrValueJson(Long spuId) {
        List<ValueSkuJsonTo> valueSkuJsonTos = baseMapper.getAllSkuValueJson(spuId);
        //转成{"118|120":"50","119|121":"50"}
        Map<String, Long> map = valueSkuJsonTos.stream()
                .collect(Collectors.toMap(ValueSkuJsonTo::getValueJson,
                        ValueSkuJsonTo::getSkuId));

        //springboot自带的jackson
        return Jsons.toStr(map);
    }
}




