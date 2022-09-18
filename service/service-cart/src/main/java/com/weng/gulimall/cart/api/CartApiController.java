package com.weng.gulimall.cart.api;

import com.weng.gulimall.cart.service.CartService;
import com.weng.gulimall.common.auth.AuthUtils;
import com.weng.gulimall.common.constant.SysRedisConst;
import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.model.cart.CartInfo;
import com.weng.gulimall.model.product.SkuInfo;
import com.weng.gulimall.model.vo.user.UserAuthInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/api/inner/rpc/cart")
public class CartApiController {

@Autowired
private RedisTemplate<String,String> redisTemplate;


    @Autowired
    private CartService cartService;

    /**
     * 把商品添加到购物车
     *
     * @return Result
     */
    @GetMapping("/addToCart")
    public Result<SkuInfo> addToCart(@RequestParam("skuId") Long skuId,
                                     @RequestParam("num") Integer num) {
        SkuInfo skuInfo = cartService.addToCart(skuId, num);

        UserAuthInfo currentAuthInfo = AuthUtils.getCurrentAuthInfo();
        if (ObjectUtils.isEmpty(currentAuthInfo.getUserId())){
            //用户未登录状态，一直使用临时购物车
            String userTempId = SysRedisConst.CART_KEY + currentAuthInfo.getUserTempId();
            redisTemplate.expire(userTempId,90, TimeUnit.DAYS);
        }

        return Result.ok(skuInfo);
    }

    /**
     * 删除购物车中选中的商品
     */
    @GetMapping("/deleteChecked")
    public Result deleteChecked(){
        cartService.deleteChecked();
        return Result.ok();
    }

    @GetMapping("/checked/list")
    public Result<List<CartInfo>> getChecked(){
        return Result.ok(cartService.getCheckedItems());
    }

}
