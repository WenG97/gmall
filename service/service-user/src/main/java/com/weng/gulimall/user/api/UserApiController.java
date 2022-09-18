package com.weng.gulimall.user.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.weng.gulimall.common.auth.AuthUtils;
import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.model.user.UserAddress;
import com.weng.gulimall.model.vo.user.UserAuthInfo;
import com.weng.gulimall.user.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/inner/rpc/user")
@RestController
public class UserApiController {


    @Autowired
    UserAddressService userAddressService;

    @GetMapping("/address/list")
    public Result<List<UserAddress>> getUserAddressList(){
        UserAuthInfo currentAuthInfo = AuthUtils.getCurrentAuthInfo();
        Long userId = currentAuthInfo.getUserId();

        //根据用户id获取所有用户地址
        LambdaQueryWrapper<UserAddress> eq = new LambdaQueryWrapper<UserAddress>().eq(UserAddress::getUserId, userId);
        return Result.ok(userAddressService.list(eq));
    }
}
