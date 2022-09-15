package com.weng.gulimall.product;

import com.weng.gulimall.product.service.SkuInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
public class nullPointTest {

    @Autowired
    private  SkuInfoService skuInfoService;

    @Test
    public  void te() {
        BigDecimal price = skuInfoService.get1010Price(101L);
        System.out.println("price = " + price);
    }


}
