package com.weng.gulimall.search.api;

import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.model.list.Goods;
import com.weng.gulimall.model.vo.search.SearchParamVo;
import com.weng.gulimall.model.vo.search.SearchResponseVo;
import com.weng.gulimall.search.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * 商品检索
     * @param searchParamVo 商品检索条件
     * @return
     */
    @PostMapping("/goods/search")
    public Result<SearchResponseVo> search(SearchParamVo searchParamVo){


        return Result.ok();
    }
}