package com.weng.gulimall.search;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;


@EnableElasticsearchRepositories
@SpringCloudApplication
public class EsMainApplication {
     public static void main(String[] args) {
             SpringApplication.run(EsMainApplication.class,args);
         }
}
