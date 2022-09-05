package com.weng.gulimall.product.service.impl;

import com.google.common.collect.Lists;

import java.util.Date;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weng.gulimall.common.constant.SysRedisConst;
import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.feign.search.SearchClient;
import com.weng.gulimall.model.list.Goods;
import com.weng.gulimall.model.list.SearchAttr;
import com.weng.gulimall.model.product.*;
import com.weng.gulimall.model.to.CategoryViewTo;
import com.weng.gulimall.model.to.SkuDetailTo;
import com.weng.gulimall.product.mapper.BaseCategory3Mapper;
import com.weng.gulimall.product.service.*;
import com.weng.gulimall.product.mapper.SkuInfoMapper;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lingzi
 * @description 针对表【sku_info(库存单元表)】的数据库操作Service实现
 * @createDate 2022-08-23 22:31:29
 */
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo>
        implements SkuInfoService {

    @Autowired
    private SpuSaleAttrService spuSaleAttrService;
    @Autowired
    private BaseCategory3Mapper baseCategory3Mapper;
    @Autowired
    private SkuImageService skuImageService;
    @Autowired
    private SkuAttrValueService skuAttrValueService;
    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private SearchClient searchClient;
    @Autowired
    private BaseTrademarkService baseTrademarkService;

    @Override
    public void savaSkuInfo(SkuInfo skuInfo) {
        //    1、sku基本信息
        save(skuInfo);
        //    2、sku图片信息
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        for (SkuImage skuImage : skuImageList) {
            skuImage.setSkuId(skuInfo.getId());
        }
        skuImageService.saveBatch(skuImageList);
        //    3、sku平台属性名和值
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        for (SkuAttrValue skuAttrValue : skuAttrValueList) {
            skuAttrValue.setSkuId(skuInfo.getId());
        }
        skuAttrValueService.saveBatch(skuAttrValueList);
        //    4、sku销售属性名和值
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList) {
            skuSaleAttrValue.setSkuId(skuInfo.getId());
            skuSaleAttrValue.setSpuId(skuInfo.getSpuId());
        }
        skuSaleAttrValueService.saveBatch(skuSaleAttrValueList);
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter(SysRedisConst.BLOOM_SKUID);
        bloomFilter.add(skuInfo.getId());
    }

    @Override
    public void onSale(Long skuId, Integer state) {
        baseMapper.updateSaleState(skuId, state);
        Goods goods = getGoodsBySkuId(skuId);
        searchClient.saveGoods(goods);
    }

    @Override
    public void cancelSale(Long skuId, Integer state) {
        baseMapper.updateSaleState(skuId, state);
        searchClient.deleteGoods(skuId);

    }

    public Goods getGoodsBySkuId(Long skuId) {
        SkuInfo skuInfo = baseMapper.selectById(skuId);

        Goods goods = new Goods();
        goods.setId(skuId);
        goods.setDefaultImg(skuInfo.getSkuDefaultImg());
        goods.setTitle(skuInfo.getSkuName());
        goods.setPrice(skuInfo.getPrice().doubleValue());
        goods.setCreateTime(new Date());
        BaseTrademark trademark = baseTrademarkService.getById(skuInfo.getTmId());
        goods.setTmId(skuInfo.getTmId());
        goods.setTmName(trademark.getTmName());
        goods.setTmLogoUrl(trademark.getLogoUrl());

        Long category3Id = skuInfo.getCategory3Id();
        CategoryViewTo categoryView = baseCategory3Mapper.getCategoryView(category3Id);
        goods.setCategory1Id(categoryView.getCategory1Id());
        goods.setCategory1Name(categoryView.getCategory1Name());
        goods.setCategory2Id(categoryView.getCategory2Id());
        goods.setCategory2Name(categoryView.getCategory2Name());
        goods.setCategory3Id(categoryView.getCategory3Id());
        goods.setCategory3Name(categoryView.getCategory3Name());

        goods.setHotScore(0L);//todo :热度更新
        List<SearchAttr> attrs = skuAttrValueService.getSkuAttrNameAndValue(skuId);
        goods.setAttrs(attrs);

        return goods;
    }

    @Deprecated
    @Override
    public SkuDetailTo getSkuDetail(Long skuId) {
        SkuDetailTo skuDetailTo = new SkuDetailTo();
        //1、通过skuId查询到skuInfo的值
        SkuInfo skuInfo = getById(skuId);
        skuDetailTo.setSkuInfo(skuInfo);
        //2、商品（sku）所属的完成分类信息
        CategoryViewTo categoryViewTo = baseCategory3Mapper.getCategoryView(skuInfo.getCategory3Id());
        skuDetailTo.setCategoryView(categoryViewTo);
        //3、skuInfo内部的图片列表
        List<SkuImage> imageList = skuImageService.getSkuImage(skuId);
        skuInfo.setSkuImageList(imageList);
        //查询价格
        BigDecimal price = get1010Price(skuId);
        skuDetailTo.setPrice(price);
        //查询当前sku 对应的spu的名值组合，并且标记isChecked属性
        List<SpuSaleAttr> spuSaleAttrList = spuSaleAttrService.getSaleAttrAndValueMarkSku(skuInfo.getSpuId(), skuId);
        skuDetailTo.setSpuSaleAttrList(spuSaleAttrList);
        //查询商品(sku)的所有兄弟产品的销售属性名和值组合在一起的关系并封装为
        // {"118|120":"50","119|121":"50"}
        String jsonTos = spuSaleAttrService.getAllSkuSaleAttrValueJson(skuInfo.getSpuId());
        skuDetailTo.setValueSkuJson(jsonTos);
        return skuDetailTo;
    }

    @Override
    public BigDecimal get1010Price(Long skuId) {

        return baseMapper.get1010Price(skuId);
    }

    @Override
    public SkuInfo getDetailSkuInfo(Long skuId) {
        return baseMapper.selectById(skuId);
    }

    @Override
    public List<SkuImage> getDetailSkuImages(Long skuId) {
        return skuImageService.getSkuImage(skuId);
    }

    @Override
    public List<Long> findAllSkuId() {

        return baseMapper.getAllSkuId();
    }
}




