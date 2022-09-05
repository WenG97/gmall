package com.weng.gulimall.model.vo.search;

import lombok.Data;

@Data
public class SearchParamVo {
    private Long category3Id;
    private Long category2Id;
    private Long category1Id;
    private String keyWord;
    private String[] props;
    private String trademark;
    private String order;
    private Integer pageNo;
}
