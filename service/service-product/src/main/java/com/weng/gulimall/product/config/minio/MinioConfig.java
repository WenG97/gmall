package com.weng.gulimall.product.config.minio;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MinioConfig {

    @Autowired
    private MinioProperties minioProperties;

    /**
     * 注入minio 客户端
     *
     * @return
     */
    @Bean
    public MinioClient minioClient() throws Exception {
        MinioClient minioClient = new MinioClient(minioProperties.getEndpoint()
                , minioProperties.getAccessKey(),
                minioProperties.getSecretKey());

        if (!minioClient.bucketExists(minioProperties.getBucketName())){
            minioClient.makeBucket(minioProperties.getBucketName());
        }
        return minioClient;
    }

}
