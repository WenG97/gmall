package com.weng.gulimall.web.controller;

import com.weng.gulimall.feign.order.OrderFeignClient;
import com.weng.gulimall.model.order.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
public class PayController {

    @Autowired
    private OrderFeignClient orderFeignClient;

    /**
     * 支付信息确认页
     *
     * @return
     */
    @GetMapping("/pay.html")
    public String payPage(@RequestParam("orderId") Long orderId, Model model) {

        OrderInfo data = orderFeignClient.getOrderInfo(orderId).getData();
        Date expireTime = data.getExpireTime();
        Date date = new Date();
        if (date.before(expireTime)) {
            //id 和 totalPrice
            model.addAttribute("orderInfo", data);
            return "payment/pay";
        }
        return "payment/error";
    }

    @GetMapping("/pay/success.html")
    public String paySuccessPage(){

        return "payment/success";
    }

}
