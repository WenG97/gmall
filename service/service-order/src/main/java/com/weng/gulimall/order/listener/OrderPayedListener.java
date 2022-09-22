package com.weng.gulimall.order.listener;

import com.rabbitmq.client.Channel;
import com.weng.gulimall.common.constant.SysRedisConst;
import com.weng.gulimall.common.util.Jsons;
import com.weng.gulimall.constant.MqConst;
import com.weng.gulimall.model.enums.ProcessStatus;
import com.weng.gulimall.model.payment.PaymentInfo;
import com.weng.gulimall.order.service.OrderInfoService;
import com.weng.gulimall.order.service.PaymentInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@Slf4j
@Service
public class OrderPayedListener {


    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private PaymentInfoService paymentInfoService;
    @Autowired
    private OrderInfoService orderInfoService;

    //todo 订单支付后，修改状态完成
    @RabbitListener(queues = MqConst.QUEUE_ORDER_PAYED)
    public void payedListener(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        String tradeNo = "x";
        try {
            Map<String, String> params = Jsons.toObj(message, Map.class);
            tradeNo = params.get("teade_no");
            log.info("监听到订单支付的消息{}，修改订单状态", tradeNo);
            //2、进行订单支付成功的修改
            //2.1、保存订单的支付消息
            PaymentInfo paymentInfo = paymentInfoService.savaPaymentInfo(params);
            //2.2、修改订单的支付状态
            //未支付->已支付
            //订单关闭->已支付(防止支付宝刚好付款成功，在回调该接口时，刚好过期的情况)
            orderInfoService.changeOrderStatus(paymentInfo.getOrderId(),
                    paymentInfo.getUserId(),
                    ProcessStatus.PAID,
                    Arrays.asList(ProcessStatus.UNPAID, ProcessStatus.CLOSED));

            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {

            log.error("订单支付业务修改失败，消息{}，失败原因：{}", message.getBody(), e);
            //应该使用lua脚本，先设置到加上过期时间的该值，在进行自增,
            // 防止因为执行失败后的重试成功后的情况造成的内存泄漏
            Long increment = redisTemplate.opsForValue().increment(SysRedisConst.MQ_RETRY + "orderpayed" + tradeNo);
            if (increment < 5) {
                channel.basicNack(deliveryTag, false, true);
            } else {
                channel.basicNack(deliveryTag, false, false);
                redisTemplate.delete(SysRedisConst.MQ_RETRY + "orderpayed" + tradeNo);

            }
        }
    }
}
