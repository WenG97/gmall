package com.weng.gulimall.constant;

public class MqConst {
    //订单交换机
    public static final String EXCHANGE_ORDER_EVENT = "order-event-exchange";
    //订单延迟队列
    public static final String QUEUE_ORDER_DELAY = "order-delay-queue";
    public static final int ORDER_CLOSE_TTL = 30 * 60;
    // public static final int ORDER_CLOSE_TTL = 10;  测试用
    public static final String RK_ORDER_DEAD = "order.dead";
    public static final String RK_ORDER_CREATED = "order.created";
    public static final String QUEUE_ORDER_DEAD = "order-dead-queue";
    public static final String RK_ORDER_PAYED = "order.payed";
    public static final String QUEUE_ORDER_PAYED = "order-payed-queue";
    //库存交换机
    public static final String EXCHANGE_WARE_EVENT = "exchange.direct.ware.stock";
    //减库存路由键
    public static final String RK_WARE_DEDUCE = "ware.stock";
    //酷讯扣减结果队列
    public static final String QUEUE_WARE_ORDER = "queue.ware.order";
    public static final String EXCHANGE_WARE_ORDER = "exchange.direct.ware.order";
    public static final String RK_WARE_ORDER = "ware.order";
}
