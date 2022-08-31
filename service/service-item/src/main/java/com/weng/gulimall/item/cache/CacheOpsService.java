package com.weng.gulimall.item.cache;


public interface CacheOpsService {
    <T> T getCacheData(String cacheKey, Class<T> clz);

    boolean bloomFilter(Long cacheKey);

    boolean tryLock(Long skuId);

    void saveData(String cacheKey, Object obj);

    void unlock(Long skuId);
}
