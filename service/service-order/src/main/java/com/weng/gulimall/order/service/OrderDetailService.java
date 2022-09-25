package com.weng.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weng.gulimall.model.order.OrderDetail;
import com.weng.gulimall.model.order.OrderInfo;
import com.weng.gulimall.model.to.mq.WareDeduceSkuInfo;

import java.util.List;

/**
* @author lingzi
* @description 针对表【order_detail(订单明细表)】的数据库操作Service
* @createDate 2022-09-15 23:51:36
*/
public interface OrderDetailService extends IService<OrderDetail> {

    List<WareDeduceSkuInfo> prepareWareDeduceSkuInfo(Long id, Long userId);

    List<OrderDetail> getOrderDetails(OrderInfo parentOrderInfo);
}
