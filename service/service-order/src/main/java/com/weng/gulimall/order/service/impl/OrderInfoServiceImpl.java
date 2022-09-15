package com.weng.gulimall.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.weng.gulimall.model.order.OrderInfo;
import com.weng.gulimall.order.service.OrderInfoService;
import com.weng.gulimall.order.mapper.OrderInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author lingzi
* @description 针对表【order_info(订单表 订单表)】的数据库操作Service实现
* @createDate 2022-09-15 23:51:36
*/
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo>
    implements OrderInfoService{

}




