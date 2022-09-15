package com.weng.gulimall.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.weng.gulimall.model.order.OrderDetail;
import com.weng.gulimall.order.service.OrderDetailService;
import com.weng.gulimall.order.mapper.OrderDetailMapper;
import org.springframework.stereotype.Service;

/**
* @author lingzi
* @description 针对表【order_detail(订单明细表)】的数据库操作Service实现
* @createDate 2022-09-15 23:51:36
*/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
    implements OrderDetailService{

}




