package com.weng.gulimall.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weng.gulimall.common.constant.SysRedisConst;
import com.weng.gulimall.common.util.Jsons;
import com.weng.gulimall.common.util.MD5;
import com.weng.gulimall.model.user.UserInfo;
import com.weng.gulimall.model.vo.user.LoginSuccessVo;
import com.weng.gulimall.user.service.UserInfoService;
import com.weng.gulimall.user.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author lingzi
 * @description 针对表【user_info(用户表)】的数据库操作Service实现
 * @createDate 2022-09-12 00:15:13
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
        implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public LoginSuccessVo login(UserInfo info) {
        //查询用户信息
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInfo::getPasswd, MD5.encrypt(info.getPasswd()))
                .eq(UserInfo::getLoginName, info.getLoginName());
        UserInfo userInfo = userInfoMapper.selectOne(wrapper);

        //用户信息不为空，表示登录成功，则返回token
        //将token与用户信息绑定，存到reids
        if (!ObjectUtils.isEmpty(userInfo)) {
            LoginSuccessVo loginSuccessVo = new LoginSuccessVo();
            String token = UUID.randomUUID().toString().replace("-", "");
            loginSuccessVo.setToken(token);
            loginSuccessVo.setNickName(userInfo.getNickName());
            redisTemplate.opsForValue().set(SysRedisConst.USER_LOGIN + token, Jsons.toStr(userInfo), 7, TimeUnit.DAYS);
            return loginSuccessVo;
        }

        return null;
    }

    @Override
    public void logout(String token) {
        redisTemplate.delete(SysRedisConst.USER_LOGIN + token);
    }
}




