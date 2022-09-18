package com.weng.gulimall.feign.order;

import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.model.vo.order.OrderConfirmDataVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/inner/rpc/order")
@FeignClient("service-order")
public interface OrderFeignClient {

    @GetMapping("/confirm/data")
    Result<OrderConfirmDataVo> getOrderConfirmData();
}
