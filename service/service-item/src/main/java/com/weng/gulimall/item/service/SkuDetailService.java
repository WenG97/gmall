package com.weng.gulimall.item.service;

import com.weng.gulimall.model.to.SkuDetailTo;

public interface SkuDetailService {

    SkuDetailTo getSkuDetail(Long skuId);

    /**
     * 更新商品热度分
     * @param skuId
     */
    void updateHotscore(Long skuId);

}
