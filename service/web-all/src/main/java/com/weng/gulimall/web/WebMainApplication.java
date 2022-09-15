package com.weng.gulimall.web;

import com.weng.gulimall.common.annotation.EnableFeignInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignInterceptor
@EnableFeignClients(basePackages = {
        "com.weng.gulimall.feign.item",
        "com.weng.gulimall.feign.product",
        "com.weng.gulimall.feign.search",
        "com.weng.gulimall.feign.cart"
})
@SpringCloudApplication
public class WebMainApplication {
     public static void main(String[] args) {
             SpringApplication.run(WebMainApplication.class,args);
         }
}
