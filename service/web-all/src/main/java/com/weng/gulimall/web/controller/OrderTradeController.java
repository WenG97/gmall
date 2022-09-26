package com.weng.gulimall.web.controller;

import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.feign.order.OrderFeignClient;
import com.weng.gulimall.model.vo.order.OrderConfirmDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class OrderTradeController {

    @Autowired
    OrderFeignClient orderFeignClient;

    @GetMapping("/trade.html")
    public String tradePage(Model model) {

        //远程调用会透传用户信息
        Result<OrderConfirmDataVo> orderConfirmData = orderFeignClient.getOrderConfirmData();
        if (!orderConfirmData.isOk()){
            return "error";
        }
        OrderConfirmDataVo data = orderConfirmData.getData();

        model.addAttribute("detailArrayList", data.getDetailArrayList());
        model.addAttribute("totalNum",data.getTotalNum());
        model.addAttribute("totalAmount",data.getTotalAmount());

        model.addAttribute("userAddressList",data.getUserAddressList());
        //追踪订单的交易号，同时避免重复提交，同时也是暴露给第三方应用
        model.addAttribute("tradeNo",data.getTradeNo());

        return "order/trade";
    }

    @GetMapping("/myOrder.html")
    public String myOrderPage(){

        return "order/myOrder";
    }
}
