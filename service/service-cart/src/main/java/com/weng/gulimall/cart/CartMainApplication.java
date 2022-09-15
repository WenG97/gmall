package com.weng.gulimall.cart;

import com.weng.gulimall.common.annotation.EnableAutoExceptionHandler;
import com.weng.gulimall.common.annotation.EnableThreadPool;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableThreadPool
@EnableAutoExceptionHandler
@EnableFeignClients(basePackages = "com.weng.gulimall.feign.product")
@SpringCloudApplication
public class CartMainApplication {
     public static void main(String[] args) {
             SpringApplication.run(CartMainApplication.class,args);
         }
}
