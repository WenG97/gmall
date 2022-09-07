package com.weng.gulimall.search;

import com.weng.gulimall.model.vo.search.SearchParamVo;
import com.weng.gulimall.model.vo.search.SearchResponseVo;
import com.weng.gulimall.search.service.GoodsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SearchTest {

    @Autowired
    GoodsService goodsService;

    @Test
    public void test01(){
        SearchParamVo searchParamVo = new SearchParamVo();
        String[] props = new String[]{"100:1281GB:机身存储","3:6GB:运行内存"};
        searchParamVo.setProps(props);
        searchParamVo.setOrder("2:asc");
        searchParamVo.setTrademark("4:小米");
       goodsService.search(searchParamVo);
    }



}
