package com.weng.gulimall.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

//这个注解包括了 服务发现和服务熔断
@SpringCloudApplication
public class GatewayMainApplication {
     public static void main(String[] args) {
             SpringApplication.run(GatewayMainApplication.class,args);
         }
}
