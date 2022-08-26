package com.weng.gulimall.model.to;

import com.weng.gulimall.model.product.SkuInfo;
import com.weng.gulimall.model.product.SpuSaleAttr;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuDetailTo {
    //当前Sku的分类信息
    private CategoryViewTo categoryView;

    private SkuInfo skuInfo;

    private List<SpuSaleAttr> spuSaleAttrList;

    private String valueSkuJson;

    private BigDecimal price;
}
