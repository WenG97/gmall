package com.weng.starter.cache;

import com.weng.starter.cache.aspect.CacheAspect;
import com.weng.starter.cache.service.CacheOpsService;
import com.weng.starter.cache.service.impl.CacheOpsServiceImpl;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@EnableAspectJAutoProxy
@AutoConfigureAfter(RedisAutoConfiguration.class)
@Configuration
public class MallCacheAutoConfiguration {

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public CacheAspect cacheAspect (){
        return new CacheAspect();
    }

    @Bean
    public CacheOpsService cacheOpsService(){
        return new CacheOpsServiceImpl();
    }

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" +redisProperties.getHost() + ":" + redisProperties.getPort())
                .setPassword(redisProperties.getPassword());

        return Redisson.create(config);
    }
}
