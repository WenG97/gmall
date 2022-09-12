package com.weng.gulimall.web.controller;

import com.weng.gulimall.common.constant.SysRedisConst;
import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.feign.cart.CartFeignClient;
import com.weng.gulimall.model.product.SkuInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CartController {


    @Autowired
    private CartFeignClient cartFeignClient;

    /**
     * 添加商品到购物车
     * @param skuId skuId
     * @param skuNum skuNum
     * @param model model
     * @return String
     */
    @GetMapping("/addCart.html")
    public String addCartHtml(@RequestParam("skuId")Long skuId,
                              @RequestParam("skuNum") Integer skuNum,
                              Model model){

        //把指定商品添加到购物车
        Result<SkuInfo> result = cartFeignClient.addToCart(skuId, skuNum);
        model.addAttribute("skuInfo",result.getData());
        model.addAttribute("skuNum",skuNum);

        return "cart/addCart";
    }
}
