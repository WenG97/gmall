package com.weng.gulimall.order.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.weng.gulimall.common.util.Jsons;
import com.weng.gulimall.model.order.OrderInfo;
import com.weng.gulimall.model.payment.PaymentInfo;
import com.weng.gulimall.order.service.OrderInfoService;
import com.weng.gulimall.order.service.PaymentInfoService;
import com.weng.gulimall.order.mapper.PaymentInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Map;

/**
 * @author lingzi
 * @description 针对表【payment_info(支付信息表)】的数据库操作Service实现
 * @createDate 2022-09-15 23:51:36
 */
@Service
public class PaymentInfoServiceImpl extends ServiceImpl<PaymentInfoMapper, PaymentInfo>
        implements PaymentInfoService {

    @Autowired
    private OrderInfoService orderInfoService;

    /**
     * 保存订单信息
     *
     * @param params
     */
    @Override
    public PaymentInfo savaPaymentInfo(Map<String, String> params) {
        String outTradeNo = params.get("out_trade_no");
        String userId = outTradeNo.substring(outTradeNo.lastIndexOf("_") + 1);
        //1、新增操作，保存到数据库之前，保证幂等性
        //2、先从数据库中获取，如果有了则不新增，保证幂等性，
        //3、也可以使用mysql的唯一键约束来保证幂等性
        PaymentInfo info = getOne(new LambdaQueryWrapper<PaymentInfo>()
                .eq(PaymentInfo::getUserId, Long.parseLong(userId))
                .eq(PaymentInfo::getOutTradeNo, outTradeNo));
        if (!ObjectUtils.isEmpty(info)) {
            return info;
        }
        //未被保存过，开始新增
        PaymentInfo paymentInfo = new PaymentInfo();

        // String outTradeNo = params.get("out_trade_no").split("_")[1];
        //用户id
        paymentInfo.setUserId(Long.parseLong(userId));
        //暴露出去的订单交易号
        paymentInfo.setOutTradeNo(outTradeNo);
        //查库获取订单id
        OrderInfo orderInfo = orderInfoService.getOrderInfoByTradeAndUserId(outTradeNo, paymentInfo.getUserId());
        //保存订单id
        paymentInfo.setOrderId(orderInfo.getId());
        //支付的类型
        paymentInfo.setPaymentType("ALIPAY");
        //支付宝此次交易的流水号
        paymentInfo.setTradeNo(params.get("trade_no"));
        //此订单的总金额
        paymentInfo.setTotalAmount(new BigDecimal(params.get("total_amount")));
        //此订单的主题
        paymentInfo.setSubject(params.get("subject"));
        //支付状态
        paymentInfo.setPaymentStatus("trade_status");

        paymentInfo.setCreateTime(new Date());
        // TemporalAccessor notify_time = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").parse(params.get("notify_time"));
        Date notifyTime = null;
        try {
            notifyTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(params.get("notify_time"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        paymentInfo.setCallbackTime(notifyTime);
        //支付宝返回的所有的文本内容
        paymentInfo.setCallbackContent(Jsons.toStr(params));

        //保存订单信息到数据库
        save(paymentInfo);
        return paymentInfo;

    }
}




