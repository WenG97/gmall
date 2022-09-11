package com.weng.gulimall.item;

import com.weng.gulimall.common.annotation.EnableThreadPool;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableThreadPool
@EnableFeignClients(basePackages = {"com.weng.gulimall.feign.product",
        "com.weng.gulimall.feign.search"})
@SpringCloudApplication
public class ItemMainApplication {
     public static void main(String[] args) {
             SpringApplication.run(ItemMainApplication.class,args);
         }
}
