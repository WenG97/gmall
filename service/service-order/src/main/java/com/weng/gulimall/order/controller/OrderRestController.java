package com.weng.gulimall.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weng.gulimall.common.auth.AuthUtils;
import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.model.order.OrderDetail;
import com.weng.gulimall.model.order.OrderInfo;
import com.weng.gulimall.model.vo.order.OrderSubmitVo;
import com.weng.gulimall.order.biz.OrderBizService;
import com.weng.gulimall.order.service.OrderDetailService;
import com.weng.gulimall.order.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/order/auth")
@RestController
public class OrderRestController {

    @Autowired
    private OrderBizService orderBizService;

    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private OrderDetailService orderDetailService;

    @PostMapping("/submitOrder")
    public Result<String> submitOrder(@RequestBody OrderSubmitVo orderSubmitVo,
                              @RequestParam("tradeNo") String tradeNo){
        Long orderID = orderBizService.submitOrder(tradeNo,orderSubmitVo);

        return Result.ok(orderID.toString());
    }

    /**
     * 查询当前用户的所有订单
     * @param pn
     * @param ps
     * @return
     */
    @GetMapping("/{pn}/{ps}")
    public Result orderList(@PathVariable("pn")Long pn,
                            @PathVariable("ps")Long ps){

        Page<OrderInfo> page = new Page<>(pn, ps);
        Long userId = AuthUtils.getCurrentAuthInfo().getUserId();
        LambdaQueryWrapper<OrderInfo> wrap = new LambdaQueryWrapper<OrderInfo>()
                .eq(OrderInfo::getUserId, userId);
        //查询当前用户的所有订单
        Page<OrderInfo> orderInfoPage = orderInfoService.page(page, wrap);
        orderInfoPage.getRecords().parallelStream()
                .forEach(o->{
                    //设置orderInfo的订单详情
                    List<OrderDetail> orderDetails = orderDetailService.getOrderDetails(o);
                    o.setOrderDetailList(orderDetails);
                });



        return Result.ok(orderInfoPage);
    }
}
