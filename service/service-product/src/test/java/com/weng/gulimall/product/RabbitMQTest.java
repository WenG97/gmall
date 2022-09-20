package com.weng.gulimall.product;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RabbitMQTest {


    @RabbitListener(queues = "")
    public void listenerTest(){

    }
}
