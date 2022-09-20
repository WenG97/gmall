package com.weng.gulimall.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.retry.support.RetryTemplate;

/**
 * 配置 rabbitTemplate的消息确认和消息回退机制
 */
@EnableRabbit //开启注解模式的rabbit
@Configuration
@Slf4j
public class AppRabbitConfiguration {

    @Bean
    public RabbitTemplate rabbitTemplate(RabbitTemplateConfigurer configurer,
                                         ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        configurer.configure(rabbitTemplate, connectionFactory);
        //设置消息确认的回调
        rabbitTemplate.setConfirmCallback((CorrelationData correlationData,
                                           boolean ack,
                                           String cause) -> {
            if (!ack){
                log.error("消息投递到交换机失败:{}:"+"\n",correlationData.getReturnedMessage());
                log.error("cause:{}:",cause);
            }
        });

        //设置消息回退的回调
        rabbitTemplate.setReturnCallback((Message message,
                                          int replyCode,
                                          String replyText,//回复的内容
                                          String exchange,
                                          String routingKey) -> {
            //消息没有被正确投递到队列
            log.error("消息投递到队列失败 "+"\n");
            log.error("message:{}"+"\n",message);
            log.error("replyCode:{}"+"\n",replyCode);
            log.error("replyText:{}"+"\n",replyText);
            log.error("exchange:{}"+"\n",exchange);
            log.error("routingKey:{}"+"\n",routingKey);

        });

        //设置消息重试次数,默认重试三次
        rabbitTemplate.setRetryTemplate(new RetryTemplate());

        return rabbitTemplate;
    }
}
