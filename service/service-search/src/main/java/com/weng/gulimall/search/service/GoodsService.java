package com.weng.gulimall.search.service;

import com.weng.gulimall.model.list.Goods;
import com.weng.gulimall.model.vo.search.SearchParamVo;
import com.weng.gulimall.model.vo.search.SearchResponseVo;

public interface GoodsService {
    void savaGoods(Goods goods);

    void deleteGoods(Long skuId);


    SearchResponseVo search(SearchParamVo searchParamVo);
}
