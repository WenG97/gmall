package com.weng.gulimall.feign.ware.callback;

import com.weng.gulimall.feign.ware.WareFeignClient;
import org.springframework.stereotype.Component;

/**
 * ware的远程调用爆炸时，的兜底方法
 */
@Component
public class WareFeignClientCallBack implements WareFeignClient {
    /**
     * 错误兜底方法
     * @param skuId
     * @param num
     * @return
     */
    @Override
    public String hasStock(Long skuId, Integer num) {

        return "1";
    }
}
