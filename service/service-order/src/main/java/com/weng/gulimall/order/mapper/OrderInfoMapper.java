package com.weng.gulimall.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weng.gulimall.model.order.OrderInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author lingzi
* @description 针对表【order_info(订单表 订单表)】的数据库操作Mapper
* @createDate 2022-09-15 23:51:36
* @Entity .OrderInfo
*/
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    void updateOrderStatus(@Param("orderId") Long orderId,
                           @Param("userId") Long userId,
                           @Param("processStatus") String processStatus,
                           @Param("orderStatus") String orderStatus,
                           @Param("expectStatus") List<String> expectStatus);
}




