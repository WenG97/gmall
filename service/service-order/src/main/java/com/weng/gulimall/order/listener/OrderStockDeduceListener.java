package com.weng.gulimall.order.listener;

import com.rabbitmq.client.Channel;
import com.weng.gulimall.common.util.Jsons;
import com.weng.gulimall.constant.MqConst;
import com.weng.gulimall.model.enums.ProcessStatus;
import com.weng.gulimall.model.order.OrderInfo;
import com.weng.gulimall.model.to.mq.WareDeduceStatusMsg;
import com.weng.gulimall.order.service.OrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

/**
 * 监听库存扣减结果，修改订单状态为以支付
 */
@Service
@Slf4j
public class OrderStockDeduceListener {


    @Autowired
    private OrderInfoService orderInfoService;

    /**
     * 监听扣减库存队列,当库存被扣减之后，修改订单状态位已出库
     *
     * @param message
     * @param channel
     */
    @RabbitListener(bindings = {@QueueBinding(
            value = @Queue(value = MqConst.QUEUE_WARE_ORDER),
            exchange = @Exchange(name = MqConst.EXCHANGE_WARE_ORDER),
            key = MqConst.RK_WARE_ORDER
    )})
    public void stockDeduceListener(Message message, Channel channel) throws IOException {
        try {
            WareDeduceStatusMsg wareDeduceStatusMsg = Jsons.toObj(message, WareDeduceStatusMsg.class);
            //订单状态为已支付，库存扣减成功，修改订单状态为等待发货
            ProcessStatus status = ProcessStatus.PAID;
            if ("DEDUCTED".equals(wareDeduceStatusMsg.getStatus())) {
                status = ProcessStatus.WAITING_DELEVER;
            } else if ("OUT_OF_STOCK".equals(wareDeduceStatusMsg.getStatus())) {
                status = ProcessStatus.STACK_OVER_EXCEPTION;
            }
            log.info("库存扣减{}, tag:{}", status, message.getMessageProperties().getDeliveryTag());

            // switch (wareDeduceStatusMsg.getStatus()){
            //     case "DEDUCTED":
            //         wareDeduceStatusMsg.
            // }
            OrderInfo orderInfo = orderInfoService.getById(wareDeduceStatusMsg.getOrderId());
            //修改订单状态
            orderInfoService.changeOrderStatus(wareDeduceStatusMsg.getOrderId(),
                    orderInfo.getUserId(),
                    status,
                    Arrays.asList(ProcessStatus.PAID));

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("修改订单状态出现异常" + e);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
        }

    }
}
