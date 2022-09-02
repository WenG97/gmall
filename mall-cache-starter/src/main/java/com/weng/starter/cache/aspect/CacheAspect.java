package com.weng.starter.cache.aspect;


import com.weng.starter.cache.annotation.GmallCache;
import com.weng.starter.cache.constant.SysRedisConst;
import com.weng.starter.cache.service.CacheOpsService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;


@Aspect
@Component
public class CacheAspect {

    //这个是线程安全的
    private SpelExpressionParser parser = new SpelExpressionParser();
    //以#{}作为定界符的一个el表达式解析器的上下文
    private TemplateParserContext parserContext = new TemplateParserContext();

    @Autowired
    private CacheOpsService cacheOpsService;

    @Around("@annotation(com.weng.starter.cache.annotation.GmallCache)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        //先查缓存
        //1、不同方法需要查询的缓存不一样
        String cacheKey = determineCacheKey(joinPoint);
        //2、查询缓存,不同方法的返回值类型不一样
        //注意大坑，实际返回的是集合的 泛型等！！！
        Type returnType = getMethodGenericReturnType(joinPoint);

        // cacheOpsService.getCacheData(cacheKey)
        //3、从缓存中查询缓存数据
        result = cacheOpsService.getCacheData(cacheKey, returnType);
        if (result == null) {
            //4、缓存中没有数据，先问布隆
            //部分场景，比如所有数据集成一条数据的，就可以不问布隆(比如三级分类)
            String bloomName = determineBloomName(joinPoint);
            //判断是否开启bloom
            if (!StringUtils.isEmpty(bloomName)) {
                //开启布隆就去查布隆
                Object bVal = determineBloomValue(joinPoint);
                boolean contains = cacheOpsService.bloomContains(bloomName, bVal);
                if (!contains) {
                    //布隆说没有，直接返回
                    return null;
                }
            }
            //不需要查布隆，或者布隆说有，准备加锁回源
            boolean lock = false;
            String lockName = "";
            try {
                //不同场景用自己的锁
                lockName = determineLockName(joinPoint);
                lock = cacheOpsService.tryLock(lockName);
                if (lock) {
                    //分布式锁加成功，准备回源
                    result = joinPoint.proceed(joinPoint.getArgs());
                    Long ttl = determineTtl(joinPoint);
                    cacheOpsService.saveData(cacheKey, result,ttl);
                    return result;
                } else {
                    TimeUnit.SECONDS.sleep(1);
                    return cacheOpsService.getCacheData(cacheKey, returnType);
                }
            } finally {
                if (lock) cacheOpsService.unlock(lockName);
            }
        }
        //查到缓存直接返回
        return result;
    }

    private Long determineTtl(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        GmallCache gmallCache = method.getDeclaredAnnotation(GmallCache.class);
        return gmallCache.ttl();
    }

    /**
     * 获取锁的名字
     *
     * @param joinPoint
     * @return
     */
    private String determineLockName(ProceedingJoinPoint joinPoint) {
        //1、拿到注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        GmallCache gmallCache = method.getDeclaredAnnotation(GmallCache.class);
        String expression = gmallCache.lockName();
        if (StringUtils.isEmpty(expression)){
            return SysRedisConst.LOCK_PREFIX +method.getName();
        }
        return evaluationExpression(expression, joinPoint, String.class);
    }

    /**
     * 获取布隆过滤器查询哪个值
     *
     * @param joinPoint
     */
    private Object determineBloomValue(ProceedingJoinPoint joinPoint) {
        //1、拿到注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        GmallCache gmallCache = signature.getMethod().getDeclaredAnnotation(GmallCache.class);
        String expression = gmallCache.bloomValue();
        return evaluationExpression(expression, joinPoint, Object.class);
    }

    /**
     * 获取布隆过滤器的名字
     *
     * @param joinPoint
     * @return
     */
    private String determineBloomName(ProceedingJoinPoint joinPoint) {
        //1、拿到注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        GmallCache gmallCache = signature.getMethod().getDeclaredAnnotation(GmallCache.class);
        String expression = gmallCache.bloomName();

        return expression;
    }

    /**
     * 获取目标方法的精确返回值类型
     *
     * @param joinPoint
     * @return
     */
    private Type getMethodGenericReturnType(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Type genericReturnType = signature.getMethod().getGenericReturnType();
        return genericReturnType;
    }

    /**
     * 根据当前连接点的信息，确定使用的cacheKey
     *
     * @param joinPoint
     * @return
     */
    private String determineCacheKey(ProceedingJoinPoint joinPoint) {
        //1、拿到注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        GmallCache gmallCache = signature.getMethod().getDeclaredAnnotation(GmallCache.class);
        String expression = gmallCache.cacheKey();
        return evaluationExpression(expression, joinPoint, String.class);
    }

    /**
     * 计算el表达式
     *
     * @param expression
     * @param joinPoint
     * @param clz
     * @param <T>
     * @return
     */
    private <T> T evaluationExpression(String expression,
                                       ProceedingJoinPoint joinPoint,
                                       Class<T> clz) {
        //1、创建一个表达式解析器
        //2、创建一个表达式解析器的上下文
        Expression exp = parser.parseExpression(expression, parserContext);
        //3、创建一个表达式计算上下文
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
        //4、给计算表达式上下文中设置要取的值
        evaluationContext.setVariable("params", joinPoint.getArgs());
        T value = exp.getValue(evaluationContext, clz);
        return value;
    }
}
