package com.weng.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weng.gulimall.model.product.BaseAttrValue;
import com.weng.gulimall.product.service.BaseAttrValueService;
import com.weng.gulimall.product.mapper.BaseAttrValueMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lingzi
 * @description 针对表【base_attr_value(属性值表)】的数据库操作Service实现
 * @createDate 2022-08-23 22:31:29
 */
@Service
public class BaseAttrValueServiceImpl extends ServiceImpl<BaseAttrValueMapper, BaseAttrValue>
        implements BaseAttrValueService {

    @Override
    public List<BaseAttrValue> getAttrValueList(Long attrId) {
        LambdaQueryWrapper<BaseAttrValue> lambdaQueryWrapper = new LambdaQueryWrapper<BaseAttrValue>()
                .eq(BaseAttrValue::getAttrId, attrId);
        return baseMapper.selectList(lambdaQueryWrapper);
    }
}




