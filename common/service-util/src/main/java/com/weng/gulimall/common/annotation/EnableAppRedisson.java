package com.weng.gulimall.common.annotation;


import com.weng.gulimall.common.config.RedissonAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RedissonAutoConfiguration.class)
public @interface EnableAppRedisson {
}
