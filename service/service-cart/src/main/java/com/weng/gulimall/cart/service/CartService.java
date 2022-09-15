package com.weng.gulimall.cart.service;

import com.weng.gulimall.model.cart.CartInfo;
import com.weng.gulimall.model.product.SkuInfo;

import java.util.List;

public interface CartService {
    /**
     * 添加到一个商品到购物车
     * @param skuId skuId
     * @param num num
     * @return SkuInfo
     */
    SkuInfo addToCart(Long skuId, Integer num);

    String determinCartKey();

    SkuInfo addItemToCart(Long skuId, Integer num ,String cartKey);

    CartInfo getItemFromCart(String cartKey, Long skuId);

    SkuInfo converCartInfo2SkuInfo(CartInfo cartInfo);

    /**
     * 转为购物车中要保存的数据模型
     *
     * @param skuInfo
     * @return CartInfo
     */
    CartInfo converSkuInfo2CartInfo(SkuInfo skuInfo);

    List<CartInfo> getCartList(String cartKey);

    void updateItemNum(Long skuId, Integer num);

    void updateChecked(Long skuId, Integer status);

    void deleteItem(Long skuId);

    void deleteChecked();

    List<CartInfo> getCheckedItems();

    void updateAllCartItemPrice(String cartKey,List<CartInfo> cartInfoList);

    String mergeUserAndTempCart();

}
