package com.weng.gulimall.order.service.impl;

import java.math.BigDecimal;

import com.weng.gulimall.common.auth.AuthUtils;
import com.weng.gulimall.common.config.threadpool.AppThreadPoolAutoConfiguration;
import com.weng.gulimall.common.constant.SysCommonConst;
import com.weng.gulimall.common.constant.SysRedisConst;
import com.weng.gulimall.common.util.Jsons;
import com.weng.gulimall.constant.MqConst;
import com.weng.gulimall.model.activity.CouponInfo;
import com.google.common.collect.Lists;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.weng.gulimall.model.enums.OrderStatus;
import com.weng.gulimall.model.enums.ProcessStatus;
import com.weng.gulimall.model.order.OrderDetail;
import com.weng.gulimall.model.order.OrderInfo;
import com.weng.gulimall.model.to.mq.OrderMsg;
import com.weng.gulimall.model.vo.order.OrderSubmitVo;
import com.weng.gulimall.order.service.OrderDetailService;
import com.weng.gulimall.order.service.OrderInfoService;
import com.weng.gulimall.order.mapper.OrderInfoMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lingzi
 * @description 针对表【order_info(订单表 订单表)】的数据库操作Service实现
 * @createDate 2022-09-15 23:51:36
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo>
        implements OrderInfoService {


    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 根据页面提提交的内容生成一个数据库的订单id
     *
     * @param orderSubmitVo orderSubmitVo
     * @return Long
     */
    @Transactional
    @Override
    public Long savaOrder(OrderSubmitVo orderSubmitVo, String tradeNo) {
        //1、封装订单信息
        OrderInfo orderInfo = prepareOrderInfo(orderSubmitVo, tradeNo);
        //1.1保存订单信息
        baseMapper.insert(orderInfo);
        //2、封装订单详情信息
        // 封装订单详情数据
        List<OrderDetail> orderDetails = prepareOrderDetail(orderSubmitVo, orderInfo);
        //2.1、保存订单详情
        orderDetailService.saveBatch(orderDetails);

        //创建订单完成，发送消息到交换机，准备延迟关单
        OrderMsg orderMsg = new OrderMsg(orderInfo.getId(), orderInfo.getUserId());
        rabbitTemplate.convertAndSend(MqConst.EXCHANGE_ORDER_EVENT,
                MqConst.RK_ORDER_CREATED,
                Jsons.toStr(orderMsg));

        //返回id,基于mp的自动填充机制
        return orderInfo.getId();
    }

    /**
     * 幂等修改关单
     *
     * @param orderId
     * @param userId
     * @param closed
     * @param expected
     */
    @Override
    public void changeOrderStatus(Long orderId, Long userId, ProcessStatus closed, List<ProcessStatus> expected) {
        String orderStatus = closed.getOrderStatus().name();
        String processStatus = closed.name();
        //todo ：修订订单的状态，完成关单操作
        baseMapper.updateOrderStatus();
    }

    /**
     * 封装订单信息详情数据
     *
     * @param orderSubmitVo orderSubmitVo
     * @param orderInfo     orderInfo
     * @return
     */
    private List<OrderDetail> prepareOrderDetail(OrderSubmitVo orderSubmitVo, OrderInfo orderInfo) {
        return orderSubmitVo.getOrderDetailList().stream()
                .map(vo -> {
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setOrderId(orderInfo.getId());
                    orderDetail.setSkuId(vo.getSkuId());

                    orderDetail.setUserId(orderInfo.getUserId());
                    orderDetail.setSkuName(vo.getSkuName());
                    orderDetail.setImgUrl(vo.getImgUrl());
                    orderDetail.setOrderPrice(vo.getOrderPrice());
                    orderDetail.setSkuNum(vo.getSkuNum());
                    orderDetail.setHasStock(vo.getHasStock());
                    orderDetail.setCreateTime(new Date());

                    orderDetail.setSplitTotalAmount(vo.getOrderPrice().multiply(vo.getOrderPrice()));


                    orderDetail.setSplitActivityAmount(new BigDecimal("0"));
                    orderDetail.setSplitCouponAmount(new BigDecimal("0"));
                    return orderDetail;
                }).collect(Collectors.toList());
    }

    /**
     * 订单信息数据的封装
     *
     * @param submitVo submitVo
     * @param tradeNo  tradeNo
     * @return
     */
    private OrderInfo prepareOrderInfo(OrderSubmitVo submitVo, String tradeNo) {
        OrderInfo info = new OrderInfo();
        info.setConsignee(submitVo.getConsignee());
        info.setConsigneeTel(submitVo.getConsigneeTel());

        info.setOrderStatus(OrderStatus.UNPAID.name());

        info.setUserId(AuthUtils.getCurrentAuthInfo().getUserId());
        info.setPaymentWay(submitVo.getPaymentWay());
        info.setDeliveryAddress(submitVo.getDeliveryAddress());
        info.setOrderComment(submitVo.getOrderComment());
        info.setOutTradeNo(tradeNo);

        info.setTradeBody(submitVo.getOrderDetailList().get(0).getSkuName());
        info.setCreateTime(new Date());

        //订单过了多久时间没支付就过期了,过期没支付就关闭订单
        info.setExpireTime(new Date(System.currentTimeMillis() + SysCommonConst.ORDER_CLOSE_TTL));

        //订单的处理状态.订单的根细粒度的状态，用于记录状态变化日志
        info.setProcessStatus(ProcessStatus.UNPAID.name());

        //物流单编号
        info.setTrackingNo("");

        //拆单场景的 父订单id
        info.setParentOrderId(0L);

        info.setImgUrl(submitVo.getOrderDetailList().get(0).getImgUrl());
        // //订单明细
        // // submitVo.getOrderDetailList().stream()
        // //                 .map(o->{
        // //                     new Orderde
        // //                 })
        // //                         .reduce();
        // info.setOrderDetailList(Lists.newArrayList());
        //仓库id
        // info.setWareId("");
        // info.setProvinceId(0L);
        info.setActivityReduceAmount(new BigDecimal("0"));
        info.setCouponAmount(new BigDecimal("0"));
        //表示的是没有没计算优惠券之前的价格
        BigDecimal totalAmount = submitVo.getOrderDetailList().stream()
                .map(o -> o.getOrderPrice().multiply(new BigDecimal(o.getSkuNum() + "")))
                .reduce(BigDecimal::add)
                .get();
        info.setOriginalTotalAmount(totalAmount);
        //订单总价格，表示减去优惠券后真实的价格
        info.setTotalAmount(totalAmount);
        //可退款日期
        info.setRefundableTime(new Date(System.currentTimeMillis() + SysCommonConst.ORDER_REFUND_TTL * 1000));
        //运费，应该对接 第三方物流平台 的api文档 ，查询收货地址的运费
        info.setFeightFee(new BigDecimal("0"));
        //
        info.setOperateTime(new Date());

        return info;
    }
}




