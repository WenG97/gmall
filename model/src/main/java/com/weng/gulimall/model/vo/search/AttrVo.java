package com.weng.gulimall.model.vo.search;

import lombok.Data;

import java.util.List;

/**
 * 封装属性列表的每个对象
 */
@Data
public class AttrVo {
    private Long attrId;
    private String  attrName;
    private List<String> attrValueList;
}
