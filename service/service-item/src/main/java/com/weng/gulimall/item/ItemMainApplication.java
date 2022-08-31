package com.weng.gulimall.item;

import com.weng.gulimall.common.annotation.EnableThreadPool;
import com.weng.gulimall.common.config.RedissonAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

@Import(RedissonAutoConfiguration.class)
@EnableThreadPool
@EnableFeignClients
@SpringCloudApplication
public class ItemMainApplication {
     public static void main(String[] args) {
             SpringApplication.run(ItemMainApplication.class,args);
         }
}
