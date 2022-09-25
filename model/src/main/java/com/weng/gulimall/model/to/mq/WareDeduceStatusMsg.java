package com.weng.gulimall.model.to.mq;

import lombok.Data;

/**
 * 扣件状态信息实体
 */
@Data
public class WareDeduceStatusMsg {

    private Long orderId;
    private String status;
}
