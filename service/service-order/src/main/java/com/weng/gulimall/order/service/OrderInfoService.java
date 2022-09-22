package com.weng.gulimall.order.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.weng.gulimall.model.enums.ProcessStatus;
import com.weng.gulimall.model.order.OrderInfo;
import com.weng.gulimall.model.vo.order.OrderSubmitVo;

import java.util.List;

/**
* @author lingzi
* @description 针对表【order_info(订单表 订单表)】的数据库操作Service
* @createDate 2022-09-15 23:51:36
*/
public interface OrderInfoService extends IService<OrderInfo> {

    Long savaOrder(OrderSubmitVo orderSubmitVo,String tradeNo);

    void changeOrderStatus(Long orderId, Long userId, ProcessStatus whileChange, List<ProcessStatus> expected);

    OrderInfo getOrderInfoByTradeAndUserId(String outTradeNo, Long userId);
}
