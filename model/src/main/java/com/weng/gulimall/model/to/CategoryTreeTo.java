package com.weng.gulimall.model.to;

import lombok.Data;

import java.util.List;

@Data
public class CategoryTreeTo {
    private Long categoryId;
    private String categoryName;
    private List<CategoryTreeTo> categoryChild;
}
