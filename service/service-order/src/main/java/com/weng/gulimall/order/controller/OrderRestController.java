package com.weng.gulimall.order.controller;

import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.model.vo.order.OrderSubmitVo;
import com.weng.gulimall.order.biz.OrderBizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/order/auth")
@RestController
public class OrderRestController {

    @Autowired
    private OrderBizService orderBizService;

    @PostMapping("/submitOrder")
    public Result submitOrder(@RequestBody OrderSubmitVo orderSubmitVo,
                              @RequestParam("tradeNo") String tradeNo){
        Long orderID = orderBizService.submitOrder(tradeNo,orderSubmitVo);

        return Result.ok(orderID);
    }
}
