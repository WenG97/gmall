package com.weng.gulimall.order.biz;

import com.weng.gulimall.model.vo.order.OrderConfirmDataVo;
import com.weng.gulimall.model.vo.order.OrderSubmitVo;

public interface OrderBizService {


    /**
     * 获取订单确认页需要的数据
     * @return
     */
    OrderConfirmDataVo getConfirmData();

    String generateTradeNo();

    boolean checkTradeNo(String tradeNo);

    Long submitOrder(String tradeNo, OrderSubmitVo orderSubmitVo);
}
