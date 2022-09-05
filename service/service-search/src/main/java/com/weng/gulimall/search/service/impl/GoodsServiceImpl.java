package com.weng.gulimall.search.service.impl;

import com.weng.gulimall.model.list.Goods;
import com.weng.gulimall.search.repository.GoodsRepository;
import com.weng.gulimall.search.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsRepository goodsRepository;

    @Override
    public void savaGoods(Goods goods) {
         goodsRepository.save(goods);
    }

    @Override
    public void deleteGoods(Long skuId) {
        goodsRepository.deleteById(skuId);
    }
}
