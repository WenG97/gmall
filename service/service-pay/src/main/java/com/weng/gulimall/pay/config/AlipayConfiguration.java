package com.weng.gulimall.pay.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AlipayProperties.class)
public class AlipayConfiguration {



    @Bean
    public AlipayClient alipayClient(AlipayProperties alipayProperties){
       return new DefaultAlipayClient(
               alipayProperties.getGatewayUrl(),
               alipayProperties.getAppId(),
               alipayProperties.getMerchantPrivateKey(),
               alipayProperties.getTransferFormat(),
               alipayProperties.getCharset(),
               alipayProperties.getAlipayPublicKey(),
               alipayProperties.getSignType()
       );
    }
}
