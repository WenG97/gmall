package com.weng.gulimall.model.vo.order;

import lombok.Data;

import java.util.List;

@Data
public class WareChildOrderVo {

    private String wareId;
    private Long orderId;//子订单系统ID
    private String consignee; //收货人
    private String consigneeTel; //收货人电话
    private String orderComment; //订单备注
    private String orderBody; //订单概要
    private String deliveryAddress; //发货地址
    private String paymentWay; //支付方式
    private List<WareChildOrderDetailItemVo> details; //当前子订单商品
}
