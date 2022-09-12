package com.weng.gulimall.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@ConfigurationProperties(prefix = "app.auth")
public class AuthUrlProperties {

    List<String> noAuthUrl;//不用登录即可访问的路径
    List<String> loginAuthUrl;//需要登录才能访问
    List<String> denyUrl; //不允许外部访问 内部接口
    String loginPage;//登录页路径
}
