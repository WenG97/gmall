package com.weng.gulimall.order.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.weng.gulimall.common.auth.AuthUtils;
import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.model.order.OrderInfo;
import com.weng.gulimall.model.vo.order.OrderConfirmDataVo;
import com.weng.gulimall.order.biz.OrderBizService;
import com.weng.gulimall.order.service.OrderInfoService;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/inner/rpc/order")
@RestController
public class OrderApiController {

    OrderBizService orderBizService;
    OrderInfoService orderInfoService;

    public OrderApiController(OrderBizService orderBizService, OrderInfoService orderInfoService) {
        this.orderInfoService = orderInfoService;
        this.orderBizService = orderBizService;
    }

    /**
     * 返回订单确认页需要的数据
     *
     * @return Result<OrderConfirmDataVo>
     */
    @GetMapping("/confirm/data")
    public Result<OrderConfirmDataVo> getOrderConfirmData() {
        OrderConfirmDataVo vo = orderBizService.getConfirmData();
        return Result.ok(vo);
    }


    /**
     * 获取到对应订单id的订单信息
     * @param orderId
     * @return
     */
    @GetMapping("/info/{orderId}")
    public Result<OrderInfo> getOrderInfo(@PathVariable("orderId") Long orderId){
        Long userId = AuthUtils.getCurrentAuthInfo().getUserId();
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<OrderInfo>().eq(OrderInfo::getId, orderId)
                .eq(OrderInfo::getUserId, userId);

        return Result.ok(orderInfoService.getOne(wrapper));
    }

}
