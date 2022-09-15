package com.weng.gulimall.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

@MapperScan("com.weng.gulimall.order.mapper")
@SpringCloudApplication
public class OrderMainApplication {

     public static void main(String[] args) {
             SpringApplication.run(OrderMainApplication.class,args);
         }
}
