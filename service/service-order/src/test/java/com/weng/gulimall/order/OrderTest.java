package com.weng.gulimall.order;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.weng.gulimall.model.order.OrderInfo;
import com.weng.gulimall.order.service.OrderInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
public class OrderTest {

    @Autowired
    private OrderInfoService orderInfoService;

    @Test
    public void testRW(){

        LambdaQueryWrapper<OrderInfo> eq = new LambdaQueryWrapper<OrderInfo>().eq(OrderInfo::getTotalAmount, new BigDecimal("1.1"));

        List<OrderInfo> list1 = orderInfoService.list(eq);
        for (OrderInfo info : list1) {
            System.out.println("info = " + info);
        }
        //
        // List<OrderInfo> list2 = orderInfoService.list(eq);
        // for (OrderInfo info : list1) {
        //     System.out.println("info = " + info);
        // }
    }


    @Test
    public void testSharding(){
        OrderInfo info1 = new OrderInfo();
        info1.setTotalAmount(new BigDecimal("7.1"));
        info1.setUserId(3L);
        orderInfoService.save(info1);


        // OrderInfo info2 = new OrderInfo();
        // info2.setTotalAmount(new BigDecimal("2.1"));
        // info2.setUserId(2L);
        // orderInfoService.save(info2);
        //
        // OrderInfo info3 = new OrderInfo();
        // info3.setTotalAmount(new BigDecimal("3.1"));
        // info3.setUserId(1L);
        // orderInfoService.save(info3);
        //
        //
        // OrderInfo info4= new OrderInfo();
        // info4.setTotalAmount(new BigDecimal("4.1"));
        // info4.setUserId(2L);
        // orderInfoService.save(info4);

    }
}
