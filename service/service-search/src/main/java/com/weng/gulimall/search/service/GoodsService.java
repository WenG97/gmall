package com.weng.gulimall.search.service;

import com.weng.gulimall.model.list.Goods;

public interface GoodsService {
    void savaGoods(Goods goods);

    void deleteGoods(Long skuId);
}
