package com.weng.gulimall.cart.service.impl;

import java.math.BigDecimal;

import com.google.common.collect.Lists;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import com.weng.gulimall.cart.service.CartService;
import com.weng.gulimall.common.auth.AuthUtils;
import com.weng.gulimall.common.constant.SysCommonConst;
import com.weng.gulimall.common.constant.SysRedisConst;
import com.weng.gulimall.common.execption.GmallException;
import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.common.result.ResultCodeEnum;
import com.weng.gulimall.common.util.Jsons;
import com.weng.gulimall.feign.product.SkuProductFeignClient;
import com.weng.gulimall.model.cart.CartInfo;
import com.weng.gulimall.model.product.SkuInfo;
import com.weng.gulimall.model.vo.user.UserAuthInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private SkuProductFeignClient skuProductFeignClient;


    @Override
    public SkuInfo addToCart(Long skuId, Integer num) {
        //1、决定购物车使用哪个键（用户id还是临时id）
        String cartKey = determinCartKey();
        //2、给购物车添加指定商品
        return addItemToCart(skuId, num, cartKey);
    }

    /**
     * 把指定商品添加到购物车
     *
     * @param skuId skuId
     * @param num   num
     * @return SkuInfo
     */
    @Override
    public SkuInfo addItemToCart(Long skuId, Integer num, String cartKey) {

        BoundHashOperations<String, String, String> cartKeyHash = redisTemplate.boundHashOps(cartKey);

        if (!cartKeyHash.hasKey(skuId.toString())) {
            //1、如果购物车之前没有这个商品,则直接将cartInfo添加倒reids中
            //从数据库中查询当前skuId的商品信息
            SkuInfo skuInfo = skuProductFeignClient.getSkuInfo(skuId).getData();
            if (ObjectUtils.isEmpty(skuInfo)) {
                throw new GmallException(ResultCodeEnum.NO_SKUNIFO);
            }
            //将skuInfo转换为购物车中保存的数据模型
            CartInfo cartInfo = converSkuInfo2CartInfo(skuInfo);
            cartInfo.setSkuId(skuId);
            cartInfo.setSkuNum(num);
            //将数据模型信息存到购物车中（此处使用redis作为购物车的存储）
            cartKeyHash.put(skuId.toString(), Jsons.toStr(cartInfo));
            return skuInfo;
        } else {
            BigDecimal sku1010Price = skuProductFeignClient.getSku1010Price(skuId).getData();
            //说明之前存在过，将之前的数据进行修改
            CartInfo cartInfo = getItemFromCart(cartKey, skuId);
            //更新数据
            cartInfo.setSkuPrice(sku1010Price);
            cartInfo.setSkuNum(cartInfo.getSkuNum() + num);
            cartInfo.setUpdateTime(new Date());
            cartKeyHash.put(skuId.toString(), Jsons.toStr(cartInfo));
            return converCartInfo2SkuInfo(cartInfo);
        }
    }

    /**
     * 将cartInfo 转换为前端需要的skuInfo数据模型
     *
     * @param cartInfo
     * @return
     */
    @Override
    public SkuInfo converCartInfo2SkuInfo(CartInfo cartInfo) {
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setSkuName(cartInfo.getSkuName());
        skuInfo.setSkuDefaultImg(cartInfo.getImgUrl());
        skuInfo.setId(cartInfo.getSkuId());
        return skuInfo;
    }

    /**
     * 从购物车中获取商品
     *
     * @param cartKey
     * @param skuId
     * @return
     */
    @Override
    public CartInfo getItemFromCart(String cartKey, Long skuId) {
        String jsonData = redisTemplate.opsForHash().get(cartKey, skuId.toString()).toString();
        return Jsons.toObj(jsonData, CartInfo.class);
    }

    /**
     * 转为购物车中要保存的数据模型
     *
     * @param skuInfo
     * @return CartInfo
     */
    @Override
    public CartInfo converSkuInfo2CartInfo(SkuInfo skuInfo) {
        CartInfo cartInfo = new CartInfo();
        cartInfo.setCartPrice(skuInfo.getPrice());
        cartInfo.setImgUrl(skuInfo.getSkuDefaultImg());
        cartInfo.setSkuName(skuInfo.getSkuName());
        cartInfo.setIsChecked(SysCommonConst.IS_CHECKED);
        cartInfo.setCreateTime(new Date());
        cartInfo.setUpdateTime(new Date());
        cartInfo.setSkuPrice(skuInfo.getPrice());
        return cartInfo;
    }


    /**
     * 获取购物车中的所有商品(按照creatTime排序)
     *
     * @param cartKey
     * @return
     */
    @Override
    public List<CartInfo> getCartList(String cartKey) {
        BoundHashOperations<String, String, String> cartHash = redisTemplate.boundHashOps(cartKey);
        List<CartInfo> collect = cartHash.values().stream()
                .map(str -> Jsons.toObj(str, CartInfo.class))
                .sorted(Comparator.comparing(CartInfo::getCreateTime))
                .collect(Collectors.toList());
        Collections.reverse(collect);
        return collect;
    }

    /**
     * 更新购物车中某个商品的数量
     *
     * @param skuId
     * @param num
     */
    @Override
    public void updateItemNum(Long skuId, Integer num) {
        String cartKey = determinCartKey();
        BoundHashOperations<String, String, String> cartHash = redisTemplate.boundHashOps(cartKey);
        CartInfo cartInfo = getItemFromCart(cartKey, skuId);
        cartInfo.setSkuNum(cartInfo.getSkuNum() + num);
        cartInfo.setUpdateTime(new Date());
        cartHash.put(skuId.toString(), Jsons.toStr(cartHash));
    }

    /**
     * 更新购物车中商品的勾选状态
     *
     * @param skuId
     * @param status
     */
    @Override
    public void updateChecked(Long skuId, Integer status) {
        String cartKey = determinCartKey();
        BoundHashOperations<String, String, String> cartHash = redisTemplate.boundHashOps(cartKey);
        CartInfo cartInfo = getItemFromCart(cartKey, skuId);
        cartInfo.setIsChecked(status);
        cartInfo.setUpdateTime(new Date());
        cartHash.put(skuId.toString(), Jsons.toStr(cartHash));
    }

    /**
     * 删除购物车中的商品
     *
     * @param skuId
     */
    @Override
    public void deleteItem(Long skuId) {
        String cartKey = determinCartKey();
        BoundHashOperations<String, String, String> cartHash = redisTemplate.boundHashOps(cartKey);
        cartHash.delete(skuId.toString());
    }

    /**
     * 删除购物车中选中的商品
     */
    @Override
    public void deleteChecked() {
        String cartKey = determinCartKey();
        BoundHashOperations<String, String, String> cartHash = redisTemplate.boundHashOps(cartKey);
        List<CartInfo> checkedItems = getCheckedItems();
        if (ObjectUtils.isEmpty(checkedItems)) {
            return;
        }
        Object[] ids = checkedItems.stream()
                .map(o -> o.getSkuId().toString())
                .toArray();

        cartHash.delete(ids);
    }

    /**
     * 获取购物车中被选中的商品
     */
    @Override
    public List<CartInfo> getCheckedItems() {
        String cartKey = determinCartKey();
        //获取选中的商品的id
        return getCartList(cartKey).stream()
                .filter(o -> o.getIsChecked().equals(SysCommonConst.IS_CHECKED))
                .collect(Collectors.toList());
    }



    /**
     * 判断当前用户登录没登录，根据状态获取临时id或者用户id
     *
     * @return String
     */
    @Override
    public String determinCartKey() {
        UserAuthInfo authInfo = AuthUtils.getCurrentAuthInfo();
        String userTempId = authInfo.getUserTempId();
        Long userId = authInfo.getUserId();
        if (!ObjectUtils.isEmpty(userId))
            return SysRedisConst.CART_KEY + userId;

        return SysRedisConst.CART_KEY + userTempId;
    }

    /**
     * 合并购物车，并返回当前是临时id还是用户id
     *
     * @return
     */
    @Override
    public String mergeUserAndTempCart() {
        UserAuthInfo currentAuthInfo = AuthUtils.getCurrentAuthInfo();
        if (!ObjectUtils.isEmpty(currentAuthInfo.getUserId()) &&
                !StringUtils.isEmpty(currentAuthInfo.getUserTempId())) {
            //合并购物车
            String tempCartKey = SysRedisConst.CART_KEY + currentAuthInfo.getUserTempId();
            List<CartInfo> tempCartList = getCartList(currentAuthInfo.getUserTempId());
            if (!ObjectUtils.isEmpty(tempCartList)){
                for (CartInfo cartInfo : tempCartList) {
                    Long skuId = cartInfo.getSkuId();
                    Integer skuNum = cartInfo.getSkuNum();
                    String userCartKey = SysRedisConst.CART_KEY + currentAuthInfo.getUserId();

                    //todo 优化一下合并逻辑,同时优化 商品种类和单个商品数量的限制

                    // addItemToCart();
                }
            }
            while (!redisTemplate.delete(tempCartKey)){}

        }
        return determinCartKey();
    }
}