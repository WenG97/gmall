package com.weng.gulimall.pay.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.weng.gulimall.common.execption.GmallException;
import com.weng.gulimall.common.result.ResultCodeEnum;
import com.weng.gulimall.common.util.DateUtil;
import com.weng.gulimall.common.util.Jsons;
import com.weng.gulimall.constant.MqConst;
import com.weng.gulimall.feign.order.OrderFeignClient;
import com.weng.gulimall.model.order.OrderInfo;
import com.weng.gulimall.pay.config.AlipayProperties;
import com.weng.gulimall.pay.service.AlipayService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AlipayServiceImpl implements AlipayService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AlipayProperties alipayProperties;

    @Autowired
    private AlipayClient alipayClient;

    @Autowired
    private OrderFeignClient orderFeignClient;

    /**
     * 生成指定订单的支付页面
     *
     * @param orderId
     * @return
     */
    @Override
    public String getAlipayPageHtml(Long orderId) throws AlipayApiException {

        //拿到订单信息
        OrderInfo orderInfo = orderFeignClient.getOrderInfo(orderId).getData();
        if (new Date().after(orderInfo.getExpireTime())) {
            throw new GmallException(ResultCodeEnum.ORDER_EXPIRED);
        }
        AlipayTradePagePayRequest alipayTradePagePayRequest = new AlipayTradePagePayRequest();
        //浏览器跳到ReturnUrl
        alipayTradePagePayRequest.setReturnUrl(alipayProperties.getReturnUrl());
        //支付宝NotifyUrl 发送请求通知支付成功消息
        alipayTradePagePayRequest.setNotifyUrl(alipayProperties.getNotifyUrl());

        //构造支付数据
        Map<String, String> bizContent = new HashMap<>();
        //对外暴露交易号
        bizContent.put("out_trade_no", orderInfo.getOutTradeNo());
        //此次需要付多少钱
        bizContent.put("total_amount", orderInfo.getTotalAmount().toString());
        //订单标题
        bizContent.put("subject", "尚品汇" + orderInfo.getOutTradeNo());
        //产品码
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        //订单体
        bizContent.put("body", orderInfo.getTradeBody());
        //配置支付宝的订单收单时间
        String expireTime = DateUtil.formatDate(orderInfo.getExpireTime(),"yyyy-MM-dd HH:mm:ss");
        bizContent.put("time_expire", expireTime);

        alipayTradePagePayRequest.setBizContent(Jsons.toStr(bizContent));
        //使用支付宝客户端给支付宝发送支付请求
        return alipayClient.pageExecute(alipayTradePagePayRequest).getBody();
    }

    /**
     * 支付宝验签
     * @param paramMaps
     * @return
     */
    @Override
    public boolean rsaCheckV1(Map<String, String> paramMaps) throws AlipayApiException {

            return AlipaySignature.rsaCheckV1(paramMaps,
                    alipayProperties.getAlipayPublicKey(),
                    alipayProperties.getCharset(),
                    alipayProperties.getSignType());
    }

    /**
     * 发送已支付的消息到Rabbit
     * @param params
     */
    @Override
    public void sendPayedMsg(Map<String, String> params) {
        rabbitTemplate.convertAndSend(MqConst.EXCHANGE_ORDER_EVENT,
                MqConst.RK_ORDER_PAYED,
                Jsons.toStr(params));
    }
}
