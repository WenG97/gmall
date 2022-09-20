package com.weng.gulimall.order.listener;

import com.rabbitmq.client.Channel;

import org.springframework.amqp.core.Message;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.stereotype.Component;

import java.io.IOException;
// @Component
public class RabbitMQTest {



    @RabbitListener(queues = "haha")
    public void listenerTest(Message message, Channel channel) throws IOException {
        message.getBody();
        System.out.println(message.getMessageProperties().getAppId());
        // System.out.println(message.getMessageProperties().getMessageId());
        // System.out.println(message.getMessageProperties().getUserId());
        // long deliveryTag = message.getMessageProperties().getDeliveryTag();
        // System.out.println("deliveryTag = " + deliveryTag);
        // MessageProperties messageProperties = message.getMessageProperties();
        // messageProperties.setAppId("appId");
        // messageProperties.setMessageId("messageId");
        // messageProperties.setUserId("userId");
        // channel.basicNack(deliveryTag,false,true);
    }
}
