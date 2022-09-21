package com.weng.gulimall.order.listener;

import com.rabbitmq.client.Channel;
import com.weng.gulimall.common.constant.SysRedisConst;
import com.weng.gulimall.common.util.Jsons;
import com.weng.gulimall.constant.MqConst;
import com.weng.gulimall.model.to.mq.OrderMsg;
import com.weng.gulimall.order.biz.OrderBizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 订单关闭监听器
 */
@Component
@Slf4j
public class OrderCloseListener {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private OrderBizService orderBizService;

    @RabbitListener(queues = MqConst.QUEUE_ORDER_DEAD)
    public void orderClose(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        //1、拿到订单消息
        OrderMsg orderMsg = Jsons.toObj(message, OrderMsg.class);

        try {
            log.info("监听到关闭订单的业务{}，正在关闭", orderMsg);
            //2、进行关单业务逻辑,注意保证幂等性
            orderBizService.closeOrder(orderMsg.getOrderId(), orderMsg.getUserId());
            log.info("{}号的订单，关闭成功", orderMsg);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {

            log.error("订单业务关闭失败，消息{}，失败原因：{}", orderMsg, e);
            Long increment = redisTemplate.opsForValue().increment(SysRedisConst.MQ_RETRY + "order" + orderMsg.getOrderId());
            if (increment < 5) {
                channel.basicNack(deliveryTag, false, true);
            } else {
                channel.basicNack(deliveryTag, false, true);
                redisTemplate.delete(SysRedisConst.MQ_RETRY + "order" + orderMsg.getOrderId());

            }
        }


    }

}
