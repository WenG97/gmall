package com.weng.gulimall.web;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = {
        "com.weng.gulimall.feign.item",
        "com.weng.gulimall.feign.product"
})
@SpringCloudApplication
public class WebMainApplication {
     public static void main(String[] args) {
             SpringApplication.run(WebMainApplication.class,args);
         }
}
