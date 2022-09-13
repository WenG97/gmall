package com.weng.gulimall.common.auth;

import com.weng.gulimall.common.constant.SysRedisConst;
import com.weng.gulimall.model.vo.user.UserAuthInfo;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AuthUtils {

    public static UserAuthInfo getCurrentAuthInfo(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest oldRequest = requestAttributes.getRequest();
        String userId = oldRequest.getHeader(SysRedisConst.USERID_HEADER);
        String userTempId = oldRequest.getHeader(SysRedisConst.USERTEMPID_HEADER);
        UserAuthInfo userAuthInfo = new UserAuthInfo();
        if (!StringUtils.isEmpty(userId)) userAuthInfo.setUserId(Long.parseLong(userId));

        userAuthInfo.setUserTempId(userTempId);
        return userAuthInfo;
    }

    // public static void main(String[] args) {
    //     List<Integer> a = new ArrayList<>();
    //     List<Integer> collect = a.stream()
    //             .map(o -> o.hashCode())
    //             .collect(Collectors.toList());
    //     Collections.reverse(collect);
    //     System.out.println(collect);
    // }
}
