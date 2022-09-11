package com.weng.gulimall.model.vo.user;

import lombok.Data;

/**
 * 用户登录成功vo
 */
@Data
public class LoginSuccessVo {
    private String token;
    private String nickName;

}
