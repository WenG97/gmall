package com.weng.gulimall.pay.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.weng.gulimall.common.execption.GmallException;
import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.common.result.ResultCodeEnum;
import com.weng.gulimall.pay.config.AlipayConfiguration;
import com.weng.gulimall.pay.service.AlipayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequestMapping("/api/payment")
@Controller
public class PayController {

    @Autowired
    private AlipayService alipayService;


    /**
     * 跳到支付宝的二维码收银台
     *
     * @param orderId
     * @return
     */
    ///api/payment/alipay/submit/779838335764922368
    @ResponseBody
    @GetMapping("/alipay/submit/{orderId}")
    public String alipayPage(@PathVariable("orderId") Long orderId) throws AlipayApiException {

        return alipayService.getAlipayPageHtml(orderId);
    }

    /**
     * 支付成功页面,同步跳转前端页面
     *
     * @param paramMaps
     * @return
     */
    @GetMapping("/paysuccess")
    public String paySuccess(@RequestParam Map<String, String> paramMaps) throws AlipayApiException {
        //对传过来的参数 验签
        if (!alipayService.rsaCheckV1(paramMaps)) {
            throw new GmallException(ResultCodeEnum.SECKILL_ILLEGAL);
        }
        return "redirect:http://gmall.com/pay/success.html";
    }

    @ResponseBody
    @RequestMapping("/success/notify")
    public String notifySuccess(@RequestParam Map<String,String> params) throws AlipayApiException {
        if (alipayService.rsaCheckV1(params)) {
            log.info("异步通知抵达，支付成功。验签通过。数据{}",params);
            //发送支付成功的消息到队列
            alipayService.sendPayedMsg(params);
        }else {
            return "error";
        }
        return "success";
    }
}
