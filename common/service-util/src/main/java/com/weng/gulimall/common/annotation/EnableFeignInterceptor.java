package com.weng.gulimall.common.annotation;


import com.weng.gulimall.common.config.FeignInterceptorConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(FeignInterceptorConfiguration.class)
public @interface EnableFeignInterceptor {
}
