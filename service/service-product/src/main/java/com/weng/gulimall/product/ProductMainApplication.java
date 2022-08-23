package com.weng.gulimall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

@MapperScan("com.weng.gulimall.product.mapper")
@SpringCloudApplication
public class ProductMainApplication {
     public static void main(String[] args) {
             SpringApplication.run(ProductMainApplication.class,args);
         }
}
