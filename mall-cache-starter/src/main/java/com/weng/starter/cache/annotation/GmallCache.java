package com.weng.starter.cache.annotation;


import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GmallCache {
    String cacheKey() default "";

    String bloomName() default "";

    String bloomValue() default "";//获取布隆过滤器判断哪个值

    String lockName() default "lock";
}
