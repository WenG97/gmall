package com.weng.gulimall.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weng.gulimall.model.product.SpuImage;
import com.weng.gulimall.model.product.SpuInfo;
import com.weng.gulimall.model.product.SpuSaleAttr;
import com.weng.gulimall.model.product.SpuSaleAttrValue;
import com.weng.gulimall.product.service.SpuImageService;
import com.weng.gulimall.product.service.SpuInfoService;
import com.weng.gulimall.product.mapper.SpuInfoMapper;
import com.weng.gulimall.product.service.SpuSaleAttrService;
import com.weng.gulimall.product.service.SpuSaleAttrValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author lingzi
* @description 针对表【spu_info(商品表)】的数据库操作Service实现
* @createDate 2022-08-23 22:31:29
*/
@Service
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoMapper, SpuInfo>
    implements SpuInfoService{

    @Autowired
    private SpuSaleAttrService spuSaleAttrService;
    @Autowired
    private SpuSaleAttrValueService spuSaleAttrValueService;
    @Autowired
    private SpuImageService spuImageService;

    @Transactional
    @Override
    public void saveSpuInfo(SpuInfo spuInfo) {
        //保存基本信息
        save(spuInfo);
        //保存spu销售属性名
        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        spuSaleAttrList.forEach(spuSaleAttr -> {
            spuSaleAttr.setSpuId(spuInfo.getId());
            //保存spu销售属性值
            List<SpuSaleAttrValue> spuSaleAttrValueList = spuSaleAttr.getSpuSaleAttrValueList();
            spuSaleAttrValueList.forEach(o->{
                o.setSpuId(spuInfo.getId());
                o.setSaleAttrName(spuSaleAttr.getSaleAttrName());
            });
            spuSaleAttrValueService.saveBatch(spuSaleAttrValueList);
        });
        spuSaleAttrService.saveBatch(spuSaleAttrList);
        //保存图片信息
        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        spuImageList.forEach(i->{
            i.setSpuId(spuInfo.getId());
        });
        spuImageService.saveBatch(spuImageList);
    }
}




