package com.weng.gulimall.product.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.model.product.BaseTrademark;
import com.weng.gulimall.product.service.BaseTrademarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/admin/product/")
@RestController
public class BaseTrademarkController {

    @Autowired
    private BaseTrademarkService baseTrademarkService;

    //分页查询品牌
    @GetMapping("baseTrademark/{pageNum}/{size}")
    public Result<Page<BaseTrademark>> baseTrademark(@PathVariable("pageNum") Long pageNum,
                                @PathVariable("size") Long size){

        Page<BaseTrademark> page1 = baseTrademarkService.page(new Page<>(pageNum,size));
        return Result.ok(page1);
    }

    //根据品牌id查询品牌信息
    @GetMapping("baseTrademark/get/{id}")
    public Result<BaseTrademark> getBaseTrademark(@PathVariable("id")Long id){
        return Result.ok(baseTrademarkService.getById(id));
    }

    @PutMapping("baseTrademark/update")
    public Result updateBaseTrademark(@RequestBody BaseTrademark baseTrademark){
        baseTrademarkService.updateById(baseTrademark);
        return Result.ok();
    }

    @PostMapping("baseTrademark/save")
    public Result saveBaseTrademark(@RequestBody BaseTrademark baseTrademark){
        baseTrademarkService.save(baseTrademark);
        return Result.ok();
    }

    @DeleteMapping("baseTrademark/remove/{tid}")
    public Result deleteBaseTrademark(@PathVariable("tid") Long tid){
        baseTrademarkService.removeById(tid);
        return Result.ok();
    }
}
