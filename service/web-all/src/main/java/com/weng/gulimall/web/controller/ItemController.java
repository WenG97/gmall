package com.weng.gulimall.web.controller;

import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.model.to.SkuDetailTo;
import com.weng.gulimall.web.feign.SkuDetailFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ItemController {

    @Autowired
    private SkuDetailFeignClient skuDetailFeignClient;

    @GetMapping("/{skuId}.html")
    public String item(@PathVariable("skuId") Long skuId,
                       Model model) {
        Result<SkuDetailTo> result = skuDetailFeignClient.getSkuDetail(skuId);
        if (result.isOk()) {
            SkuDetailTo skuDetailTo = result.getData();
            if (skuDetailTo ==null || skuDetailTo.getSkuInfo()==null){
                return "item/404";
            }
            //查询商品详情
            //1、商品的完整分类信息
            model.addAttribute("categoryView", skuDetailTo.getCategoryView());
            //2、商品的sku基本信息
            model.addAttribute("skuInfo", skuDetailTo.getSkuInfo());
            //3、商品的（sku）的图片信息
            model.addAttribute("spuSaleAttrList", skuDetailTo.getSpuSaleAttrList());
            //4、商品（sku）定义时的spu属性名和值的信息。标识当前sku，页面高亮
            model.addAttribute("valuesSkuJson",skuDetailTo.getValueSkuJson());
            //5.价格
            model.addAttribute("price",skuDetailTo.getPrice());
        }


        return "item/index";
    }
}
