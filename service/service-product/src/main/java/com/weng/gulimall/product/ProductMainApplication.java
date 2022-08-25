package com.weng.gulimall.product;

import com.weng.gulimall.common.config.Swagger2Config;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.Import;

@MapperScan("com.weng.gulimall.*.mapper")
@Import(Swagger2Config.class)
@SpringCloudApplication
public class ProductMainApplication {
     public static void main(String[] args) {
             SpringApplication.run(ProductMainApplication.class,args);
         }
}
