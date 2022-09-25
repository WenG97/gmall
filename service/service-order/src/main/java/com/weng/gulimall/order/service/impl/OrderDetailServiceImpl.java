package com.weng.gulimall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.weng.gulimall.model.order.OrderDetail;
import com.weng.gulimall.model.order.OrderInfo;
import com.weng.gulimall.model.to.mq.WareDeduceSkuInfo;
import com.weng.gulimall.order.service.OrderDetailService;
import com.weng.gulimall.order.mapper.OrderDetailMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author lingzi
* @description 针对表【order_detail(订单明细表)】的数据库操作Service实现
* @createDate 2022-09-15 23:51:36
*/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
    implements OrderDetailService{

    /**
     * 查询订单明细，并且转换为仓库需要的数据
     * @param orderId
     * @param userId
     * @return
     */
    @Override
    public List<WareDeduceSkuInfo> prepareWareDeduceSkuInfo(Long orderId, Long userId) {
        List<OrderDetail> orderDetails = baseMapper.selectList(new LambdaQueryWrapper<OrderDetail>()
                .eq(OrderDetail::getOrderId, orderId)
                .eq(OrderDetail::getUserId, userId));

        return orderDetails.stream().map(o -> {
            WareDeduceSkuInfo wareDeduceSkuInfo = new WareDeduceSkuInfo();
            wareDeduceSkuInfo.setSkuId(o.getSkuId());
            wareDeduceSkuInfo.setSkuNum(o.getSkuNum().toString());
            wareDeduceSkuInfo.setSkuName(o.getSkuName());
            return wareDeduceSkuInfo;
        }).collect(Collectors.toList());
    }

    /**
     * 在分单时，查询订单明细
     * @param parentOrderInfo
     * @return
     */
    @Override
    public List<OrderDetail> getOrderDetails(OrderInfo parentOrderInfo) {
        return baseMapper.selectList(new LambdaQueryWrapper<OrderDetail>()
                .eq(OrderDetail::getUserId, parentOrderInfo.getUserId())
                .eq(OrderDetail::getOrderId, parentOrderInfo.getId()));
    }
}




