package com.weng.gulimall.order.api;

import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.model.vo.order.OrderConfirmDataVo;
import com.weng.gulimall.order.biz.OrderBizService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/inner/rpc/order")
@RestController
public class OrderApiController {

    OrderBizService orderBizService;

    public OrderApiController(OrderBizService orderBizService) {
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

}
