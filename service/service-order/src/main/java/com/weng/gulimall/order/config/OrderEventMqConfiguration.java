package com.weng.gulimall.order.config;

import com.weng.gulimall.constant.MqConst;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置订单交换机和队列
 * 如果项目启动发现没有这个交换机个队列就会自动创建出来
 */
@Configuration
public class OrderEventMqConfiguration {

    /**
     * 创建订单交换机
     * @return
     */
    @Bean
    public Exchange orderExchange(){
        //autoDelete()默认不自动删除
        //durable() 默认持久化
        //ignoreDeclarationExceptions 默认忽略已声明异常
        //internal 默认不是内部交换机
        return ExchangeBuilder.topicExchange(MqConst.EXCHANGE_ORDER_EVENT)
                .build();
    }

    /**
     * 延迟队列
     * @return
     */
    @Bean
    public Queue orderDelayQueue(){
        // exclusive() 默认是false
        //autoDelete 默认是false
        return QueueBuilder.durable(MqConst.QUEUE_ORDER_DELAY)
                .ttl(MqConst.ORDER_CLOSE_TTL*1000)
                .deadLetterExchange(MqConst.EXCHANGE_ORDER_EVENT)
                .deadLetterRoutingKey(MqConst.RK_ORDER_DEAD)
                .build();
    }

    /**
     * 延迟队列和交换机绑定
     * @return
     */
    @Bean
    public Binding orderDelayQueueBinding(@Qualifier("orderDelayQueue") Queue queue,
                                          @Qualifier("orderExchange") Exchange exchange){
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(MqConst.RK_ORDER_CREATED)
                .noargs();
    }


    /**
     * 死单duil
     */
    @Bean
    public Queue orderDeadQueue(){
        return QueueBuilder.durable(MqConst.QUEUE_ORDER_DEAD).build();
    }

    /**
     * 死单队列和订单交换机
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding orderDeadQueueBinding(@Qualifier("orderDeadQueue") Queue queue,
                                         @Qualifier("orderExchange") Exchange exchange){
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(MqConst.RK_ORDER_DEAD)
                .noargs();
    }
}
