package com.weng.starter.cache.service;


import java.lang.reflect.Type;

public interface CacheOpsService {
    /**
     * 从缓存获取json，并转换为一个普通对象
     * @param cacheKey
     * @param clz
     * @param <T>
     * @return
     */
    <T> T getCacheData(String cacheKey, Class<T> clz);

    /**
     * 从缓存中获取json，并转换为复杂泛型对象
     * @param cacheKey
     * @param type
     * @return
     */
    Object getCacheData(String cacheKey, Type type);

    boolean bloomContains(Long skuId);

    /**
     * 指定布隆过滤器里面是否包含指定值
     * @param bloomName
     * @param bVal
     * @return
     */
    boolean bloomContains(String bloomName, Object bVal);

    boolean tryLock(Long skuId);

    /**
     *尝试加上指定的锁名
     * @param lockName
     * @return
     */
    boolean tryLock(String  lockName);

    void saveData(String cacheKey, Object obj);

    void unlock(Long skuId);

    /**
     * 解指定的锁
     * @param lockName
     */
    void unlock(String  lockName);

}
