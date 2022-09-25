package com.weng.gulimall.order.biz;

import com.weng.gulimall.model.vo.order.OrderConfirmDataVo;
import com.weng.gulimall.model.vo.order.OrderSubmitVo;
import com.weng.gulimall.model.vo.order.OrderWareMapVo;
import com.weng.gulimall.model.vo.order.WareChildOrderVo;

import java.util.List;

public interface OrderBizService {


    /**
     * 获取订单确认页需要的数据
     * @return
     */
    OrderConfirmDataVo getConfirmData();

    String generateTradeNo();

    boolean checkTradeNo(String tradeNo);

    Long submitOrder(String tradeNo, OrderSubmitVo orderSubmitVo);

    void closeOrder(Long orderId, Long userId);

    List<WareChildOrderVo> orderSplit(OrderWareMapVo vo);
}
