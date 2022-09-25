package com.weng.gulimall.model.vo.order;

import lombok.Data;

import java.util.List;

@Data
public class WareMapItem {

    private Long wareId;
    private List<Long> skuIds;
}
