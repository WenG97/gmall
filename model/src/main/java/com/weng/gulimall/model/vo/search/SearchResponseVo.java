package com.weng.gulimall.model.vo.search;

import com.weng.gulimall.model.list.Goods;
import com.weng.gulimall.model.list.SearchAttr;
import lombok.Data;

import java.util.List;

@Data
public class SearchResponseVo {

    //检索用的所有参数
    private SearchParamVo searchParam;
    //品牌面包屑
    private String trademarkParam;
    //平台属性面包屑
    private List<SearchAttr> propsParamList;
    //以上面包屑功能ok

    //所有品牌列表
    private List<TrademarkVo> trademarkList;
    //所有属性列表
    private List<AttrVo> attrsList;
    //以上筛选列表ok

    //排序信息
    private OrderMapVo orderMap;
    //检索到的商品集合
    private List<Goods> goods;
    //页码信息
    private Integer pageNo;
    //总页码
    private Integer totalPages;
    //老连接
    private String urlParam;
}
