package com.weng.gulimall.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.weng.gulimall.model.payment.PaymentInfo;
import com.weng.gulimall.order.service.PaymentInfoService;
import com.weng.gulimall.order.mapper.PaymentInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author lingzi
* @description 针对表【payment_info(支付信息表)】的数据库操作Service实现
* @createDate 2022-09-15 23:51:36
*/
@Service
public class PaymentInfoServiceImpl extends ServiceImpl<PaymentInfoMapper, PaymentInfo>
    implements PaymentInfoService{

}




