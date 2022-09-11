package com.weng.gulimall.user.controller;

import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.common.result.ResultCodeEnum;
import com.weng.gulimall.model.user.UserInfo;
import com.weng.gulimall.model.vo.user.LoginSuccessVo;
import com.weng.gulimall.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserInfoService userInfoService;


    /**
     * 用户登录
     * @param userInfo userInfo
     * @return
     */
    @PostMapping("/passport/login")
    public Result login(@RequestBody UserInfo userInfo){

        LoginSuccessVo loginSuccessVo = userInfoService.login(userInfo);
        if (!ObjectUtils.isEmpty(loginSuccessVo)){
            return Result.ok(loginSuccessVo);
        }else {
            return Result.build(null,ResultCodeEnum.LOGIN_ERROR);
        }
    }

    @GetMapping("/passport/logout")
    public Result logout(@RequestHeader("token") String token){
        userInfoService.logout(token);
        return Result.ok();
    }
}
