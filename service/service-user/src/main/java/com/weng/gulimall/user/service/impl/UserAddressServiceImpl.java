package com.weng.gulimall.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weng.gulimall.model.user.UserAddress;
import com.weng.gulimall.user.service.UserAddressService;
import com.weng.gulimall.user.mapper.UserAddressMapper;
import org.springframework.stereotype.Service;

/**
* @author lingzi
* @description 针对表【user_address(用户地址表)】的数据库操作Service实现
* @createDate 2022-09-12 00:15:13
*/
@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress>
    implements UserAddressService{

}




