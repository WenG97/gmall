package com.weng.gulimall.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

@MapperScan(basePackages = "com.weng.gulimall.user.mapper")
@SpringCloudApplication
public class UserMainApplication {
     public static void main(String[] args) {
             SpringApplication.run(UserMainApplication.class,args);
         }
}
