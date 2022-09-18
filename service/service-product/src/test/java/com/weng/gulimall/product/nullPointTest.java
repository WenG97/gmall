package com.weng.gulimall.product;

import com.weng.gulimall.product.service.SkuInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;


public class nullPointTest {
    public static void main(String[] args) throws InterruptedException {
        tets tets = new tets();
        Thread a1 = new Thread(() -> {
            while (tets.a) {
                System.out.println(tets.a);
            }
        });

        Thread a2 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tets.a = false;
        });
        a1.start();
        a2.start();

        try {
            TimeUnit.SECONDS.sleep(999999991);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
class  tets{
    boolean a =true;

}