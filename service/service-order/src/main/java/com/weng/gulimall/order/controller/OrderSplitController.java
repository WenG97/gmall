package com.weng.gulimall.order.controller;

import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.model.vo.order.OrderWareMapVo;
import com.weng.gulimall.model.vo.order.WareChildOrderVo;
import com.weng.gulimall.order.biz.OrderBizService;
import com.weng.gulimall.order.listener.OrderPayedListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderSplitController {

    private static final Logger log = LoggerFactory.getLogger(OrderSplitController.class);

    @Autowired
    private OrderBizService orderBizService;

    /**
     * 拆单
     * @param vo
     * @return
     */
    @PostMapping("/orderSplit")
    public List<WareChildOrderVo> orderSplit(OrderWareMapVo vo){
        log.info("拆单请求到达");
        return orderBizService.orderSplit(vo);
    }
}
