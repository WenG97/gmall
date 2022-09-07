package com.weng.gulimall.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;

//这个注解包括了 服务发现和服务熔断
@SpringCloudApplication
public class GatewayMainApplication {
     public static void main(String[] args ) {
         SpringApplication.run(GatewayMainApplication.class,args);
         }
}
