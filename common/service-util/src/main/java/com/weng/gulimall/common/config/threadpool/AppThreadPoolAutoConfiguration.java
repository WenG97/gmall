package com.weng.gulimall.common.config.threadpool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@EnableConfigurationProperties(AppThreadPoolProperties.class)
@Configuration
public class AppThreadPoolAutoConfiguration {

    @Autowired
    private AppThreadPoolProperties appThreadPoolProperties;
    @Value("${spring.application.name}")
    private String appName;

    @Bean
    public ThreadPoolExecutor coreExecutor(){
        return new ThreadPoolExecutor(
                appThreadPoolProperties.getCorePoolSize(),
                appThreadPoolProperties.getMaximumPoolSize(),
                appThreadPoolProperties.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(appThreadPoolProperties.getQueueSize()),
                new ThreadFactory() {
                    int i;
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r,appName + ":core-thread" + i++);
                    }
                },
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

}
