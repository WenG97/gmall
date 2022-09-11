package com.weng.gulimall.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weng.gulimall.model.user.UserInfo;
import com.weng.gulimall.model.vo.user.LoginSuccessVo;

/**
* @author lingzi
* @description 针对表【user_info(用户表)】的数据库操作Service
* @createDate 2022-09-12 00:15:13
*/
public interface UserInfoService extends IService<UserInfo> {

    LoginSuccessVo login(UserInfo userInfo);

    void logout(String token);
}
