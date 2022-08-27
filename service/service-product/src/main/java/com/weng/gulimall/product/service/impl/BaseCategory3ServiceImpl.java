package com.weng.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weng.gulimall.model.product.BaseCategory2;
import com.weng.gulimall.model.product.BaseCategory3;
import com.weng.gulimall.model.to.CategoryViewTo;
import com.weng.gulimall.product.service.BaseCategory3Service;
import com.weng.gulimall.product.mapper.BaseCategory3Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author lingzi
* @description 针对表【base_category3(三级分类表)】的数据库操作Service实现
* @createDate 2022-08-23 04:25:08
*/
@Service
public class BaseCategory3ServiceImpl extends ServiceImpl<BaseCategory3Mapper, BaseCategory3>
    implements BaseCategory3Service{

    @Autowired
    private BaseCategory3Mapper baseCategory3Mapper;

    @Override
    public List<BaseCategory3> getCategory1Child(Long c2id) {
        return  baseCategory3Mapper.selectList(new LambdaQueryWrapper<BaseCategory3>().eq(BaseCategory3::getCategory2Id, c2id));
    }

    @Override
    public CategoryViewTo getCategoryView(Long c3Id) {
        return baseCategory3Mapper.getCategoryView(c3Id);
    }
}




