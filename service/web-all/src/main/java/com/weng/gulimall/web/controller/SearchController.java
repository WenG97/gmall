package com.weng.gulimall.web.controller;

import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.feign.search.SearchClient;
import com.weng.gulimall.model.vo.search.SearchParamVo;
import com.weng.gulimall.model.vo.search.SearchResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SearchController {

    @Autowired
    private SearchClient searchClient;

    @GetMapping("/list.html")
    public String search(SearchParamVo searchParamVo, Model model) {
        Result<SearchResponseVo> search = searchClient.search(searchParamVo);

        //把Result的数返回到页面
        SearchResponseVo data = search.getData();
        model.addAttribute("searchParam",data.getSearchParam());
        model.addAttribute("trademarkParam",data.getTrademarkParam());
        model.addAttribute("propsParamList",data.getPropsParamList());
        model.addAttribute("trademarkList",data.getTrademarkList());
        model.addAttribute("attrsList",data.getAttrsList());

        //排序信息
        model.addAttribute("orderMap",data.getOrderMap());
        //所有商品列表
        model.addAttribute("goodsList",data.getGoods());
        //分页信息
        model.addAttribute("pageNo",data.getPageNo());
        model.addAttribute("totalPages",data.getTotalPages());
        //url信息
        model.addAttribute("urlParam",data.getUrlParam());


        return "list/index";
    }
}
