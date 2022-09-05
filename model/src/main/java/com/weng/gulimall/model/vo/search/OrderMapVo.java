package com.weng.gulimall.model.vo.search;

import lombok.Data;

@Data
public class OrderMapVo {
    //1、是综合   2、是价格
    private String type;
    //排序规则：降序还是升序
    private String order;
}
