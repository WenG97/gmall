package com.weng.gulimall.common.config.threadpool;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.thread-pool")
public class AppThreadPoolProperties {

    private int corePoolSize = 2;
    private int maximumPoolSize = 4;
    private Integer queueSize = 200;
    private long keepAliveTime = 300;


}
