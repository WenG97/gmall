package com.weng.gulimall.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.alipay")
public class AlipayProperties {
    //阿里网关地址
    private String gatewayUrl;
    private String appId;
    //引用私钥
    private String merchantPrivateKey;
    //字符编码
    private String charset;
    //阿里公钥
    private String alipayPublicKey;
    //签名方式
    private String signType;
    //同步跳转地址
    private String returnUrl;
    //异步通知地址
    private String notifyUrl;
    //传出格式
    private String transferFormat;

}
