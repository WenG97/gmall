package com.weng.gulimall.cart.api;

import com.weng.gulimall.common.constant.SysRedisConst;
import com.weng.gulimall.common.result.Result;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/inner/rpc/cart")
public class CartApiController {

    /**
     * 把商品添加到购物车
     * @return Result
     */
    @GetMapping("/addToCart")
    public Result addToCart(@RequestParam("skuId")Long skuId,
                            @RequestParam("num")Integer num,
                            @RequestHeader(value = SysRedisConst.USERID_HEADER , required = false) String userId,
                            @RequestHeader(value = SysRedisConst.USERTEMPID_HEADER,required = false) String userTempId){

        return Result.ok();
    }
}
