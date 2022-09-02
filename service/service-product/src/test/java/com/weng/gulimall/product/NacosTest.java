package com.weng.gulimall.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class NacosTest {

    @Value("${aaa}")
    String aa;

    @Test
    public void test01(){
        System.out.println("aa = " + aa);
    }

}
