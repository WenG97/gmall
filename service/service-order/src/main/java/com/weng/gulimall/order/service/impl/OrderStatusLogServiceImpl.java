package com.weng.gulimall.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.weng.gulimall.model.order.OrderStatusLog;
import com.weng.gulimall.order.service.OrderStatusLogService;
import com.weng.gulimall.order.mapper.OrderStatusLogMapper;
import org.springframework.stereotype.Service;

/**
* @author lingzi
* @description 针对表【order_status_log】的数据库操作Service实现
* @createDate 2022-09-15 23:51:36
*/
@Service
public class OrderStatusLogServiceImpl extends ServiceImpl<OrderStatusLogMapper, OrderStatusLog>
    implements OrderStatusLogService{

}




