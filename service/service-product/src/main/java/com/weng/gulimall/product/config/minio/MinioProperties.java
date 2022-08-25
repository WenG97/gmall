package com.weng.gulimall.product.config.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.minio")
public class MinioProperties {
    /*
minio:
  endpoint: http://192.168.41.101:9001 # minio 服务地址
  accessKey: admin # minio Access key就像用户ID，可以唯一标识你的账户
  secretKey: admin123456 # minio Secret key是你账户的密码
  bucketName: gmall # minio 桶名称
 */
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
}
