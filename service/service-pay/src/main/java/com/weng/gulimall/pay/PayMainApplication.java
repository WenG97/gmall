package com.weng.gulimall.pay;


import com.weng.gulimall.annotation.EnableAppRabbit;
import com.weng.gulimall.common.annotation.EnableAutoExceptionHandler;
import com.weng.gulimall.common.annotation.EnableFeignInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableAppRabbit
@EnableAutoExceptionHandler
@EnableFeignInterceptor
@EnableFeignClients("com.weng.gulimall.feign.order")
@SpringCloudApplication
public class PayMainApplication {
     public static void main(String[] args) {
             SpringApplication.run(PayMainApplication.class,args);
         }
}
