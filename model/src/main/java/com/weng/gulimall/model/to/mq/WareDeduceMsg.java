package com.weng.gulimall.model.to.mq;

import lombok.Data;

import java.util.List;

@Data
public class WareDeduceMsg {

    private Long orderId;
    private String consignee;
    private String consigneeTel;
    private String orderComment;
    private String orderBody;
    private String deliveryAddress;
    private String paymentWay = "2";

    List<WareDeduceSkuInfo> details;


}
