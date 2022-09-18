package com.weng.gulimall.feign.user;

import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.model.user.UserAddress;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/inner/rpc/user")
@FeignClient("service-user")
public interface UserFeignClient {

    @GetMapping("/address/list")
    Result<List<UserAddress>> getUserAddressList();
}
