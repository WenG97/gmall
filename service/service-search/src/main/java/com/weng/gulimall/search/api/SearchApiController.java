package com.weng.gulimall.search.api;

import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.model.list.Goods;
import com.weng.gulimall.search.repository.GoodsRepository;
import com.weng.gulimall.search.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/inner/rpc/search")
@RestController
public class SearchApiController {

    @Autowired
    private GoodsService goodsService;


    /**
     * 保存商品信息到es
     * @param goods
     * @return
     */
    @PostMapping("/goods")
    public Result saveGoods (@RequestBody Goods goods){
        goodsService.savaGoods(goods);
        return Result.ok();
    }

    @DeleteMapping("/delete/{skuId}")
    public Result deleteGoods (@PathVariable("skuId") Long skuId){
        goodsService.deleteGoods(skuId);
        return Result.ok();
    }
}
