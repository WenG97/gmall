package com.weng.gulimall.cart.controller;

import com.weng.gulimall.cart.service.CartService;
import com.weng.gulimall.common.auth.AuthUtils;
import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.model.cart.CartInfo;
import com.weng.gulimall.model.vo.user.UserAuthInfo;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartRestController {

    @Autowired
    private CartService cartService;

    /**
     * 查看当前用户的 购物车列表
     *
     * @return
     */
    @GetMapping("/cartList")
    public Result<List<CartInfo>> cartList() {
        // String cartKey = cartService.determinCartKey();
        //其实合并key应该在用户登录后后，使用异步合并，或者消息中间件合并
            String cartKey = cartService.mergeUserAndTempCart();
        //获取购物车中的所有商品
        List<CartInfo> cartInfoList = cartService.getCartList(cartKey);
        return Result.ok(cartInfoList);
    }

    /**
     * 修改购物车中商品的数量
     *
     * @param skuId
     * @param num
     * @return
     */
    @PostMapping("/addToCart/{skuId}/{num}")
    public Result updateItemNum(@PathVariable("skuId") Long skuId,
                                @PathVariable("num") Integer num) {

        cartService.updateItemNum(skuId, num);
        return Result.ok();
    }

    @GetMapping("/check/{skuId}/{status}")
    public Result check(@PathVariable("skuId") Long skuId,
                        @PathVariable("status") Integer status) {

        cartService.updateChecked(skuId,status);
        return Result.ok();
    }

    @DeleteMapping("/deleteCart/{skuId}")
    public Result deleteItem(@PathVariable("skuId") Long skuId) {

        cartService.deleteItem(skuId);
        return Result.ok();
    }
}
