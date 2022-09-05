package com.weng.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weng.gulimall.model.product.BaseAttrInfo;
import com.weng.gulimall.model.product.BaseAttrValue;
import com.weng.gulimall.product.mapper.BaseAttrValueMapper;
import com.weng.gulimall.product.service.BaseAttrInfoService;
import com.weng.gulimall.product.mapper.BaseAttrInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lingzi
 * @description 针对表【base_attr_info(属性表)】的数据库操作Service实现
 * @createDate 2022-08-23 22:31:29
 */
@Service
public class BaseAttrInfoServiceImpl extends ServiceImpl<BaseAttrInfoMapper, BaseAttrInfo>
        implements BaseAttrInfoService {

    @Autowired
    private BaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    private BaseAttrValueMapper baseAttrValueMapper;

    @Override
    public List<BaseAttrInfo> getAttrInfoByCategoryId(Long c1id, Long c2id, Long c3id) {
        return baseAttrInfoMapper.getAttrInfoByCategoryId(c1id, c2id, c3id);
    }

    @Override
    public void saveOrUpdateAttrInfo(BaseAttrInfo baseAttrInfo) {
        if (baseAttrInfo.getId() == null) {
            //    id为null  表示是新增操作
            addBaseAttrInfo(baseAttrInfo);
        } else {
            updateBaseAttrInfo(baseAttrInfo);
        }
    }

    private void updateBaseAttrInfo(BaseAttrInfo baseAttrInfo) {
        //修改属性名的信息
        baseAttrInfoMapper.updateById(baseAttrInfo);

        //    id不为null  ， 改动baseAttrInfo下的baseAttrValue的值
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        LambdaQueryWrapper<BaseAttrValue> lambdaQueryWrapper =
                new LambdaQueryWrapper<BaseAttrValue>().
                        eq(BaseAttrValue::getAttrId, baseAttrInfo.getId());
        if (attrValueList.size() == 0) { 
            //表示当前BaseAttrInfo下的value没有数据,不用在判断新增或者修改
            baseAttrValueMapper.delete(lambdaQueryWrapper);
        } else {
            //说明当前attrValueList不为空， 那么删除此次没传过来的数据
            List<Long> vids = new ArrayList<>();
            attrValueList.forEach(o -> {
                if (o.getId()!=null)
                vids.add(o.getId());
            });
            if (vids.size() > 0) { 
                //删除此次没传过来的数据
                lambdaQueryWrapper.notIn(BaseAttrValue::getId, vids);
                baseAttrValueMapper.delete(lambdaQueryWrapper);
            }else {
            //    如果等于0，说明 之前的所有都被丢弃
                baseAttrValueMapper.delete(lambdaQueryWrapper);
            }

            //表示attrValue有值， 判断哪些数据该 改  或新增
            attrValueList.forEach(value -> {
                if (value.getId() == null) {
                    // value的id为null 表示当前value是新增
                    value.setAttrId(baseAttrInfo.getId());
                    baseAttrValueMapper.insert(value);
                } else {
                    baseAttrValueMapper.updateById(value);
                }
            });
        }
    }

    private void addBaseAttrInfo(BaseAttrInfo baseAttrInfo) {
        //保存baseAttrInfo
        baseAttrInfoMapper.insert(baseAttrInfo);
        Long id = baseAttrInfo.getId();
        if (baseAttrInfo.getAttrName()!=null){  //避免出现空值报错
            //保存baseAttrValue
            baseAttrInfo.getAttrValueList().forEach(value -> {
                value.setAttrId(id);
                baseAttrValueMapper.insert(value);
            });
        }
    }
}




