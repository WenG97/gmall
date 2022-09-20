package com.weng.gulimall.order;

import com.weng.gulimall.annotation.EnableAppRabbit;
import com.weng.gulimall.common.annotation.EnableAutoExceptionHandler;
import com.weng.gulimall.common.annotation.EnableFeignInterceptor;
import com.weng.gulimall.common.annotation.EnableThreadPool;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableAppRabbit
@EnableThreadPool
@EnableTransactionManagement
@EnableAutoExceptionHandler
@EnableFeignInterceptor
@EnableFeignClients({"com.weng.gulimall.feign.cart"
        , "com.weng.gulimall.feign.user",
        "com.weng.gulimall.feign.product",
        "com.weng.gulimall.feign.ware"})
@MapperScan("com.weng.gulimall.order.mapper")
@SpringCloudApplication
public class OrderMainApplication {
     public static void main(String[] args) {
             SpringApplication.run(OrderMainApplication.class,args);
         }
}
