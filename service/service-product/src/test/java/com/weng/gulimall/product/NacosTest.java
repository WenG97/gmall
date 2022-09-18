package com.weng.gulimall.product;

import com.weng.gulimall.model.to.SkuDetailTo;
import io.swagger.models.auth.In;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Base64Utils;

import java.lang.ref.WeakReference;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
public class NacosTest {

    int a = 6;
    // @Value("${aaa}")
    // String aa;
    //
    // @Test
    // public void test01(){
    //     System.out.println("aa = " + aa);
    // }

    // static Object obj = new Object();
    // static Object aObj = new Object();
    //
    // public static void main(String[] args) throws InterruptedException {
    //     List<Object> a = new ArrayList<>();
    //     a.add(1);
    //     a.add(2);
    //     a.add(13);
    //     a.add(14);
    //     a.add(61);
    //     a.add(17);
    //     a.add(15);
    //     a.add(17);
    //     a.add(81);
    //     Base64.Encoder encoder = Base64.getEncoder();
    //     Base64.Decoder decoder = Base64.getDecoder();
    // }
    //
    //
    // private static void product() {
    //     synchronized (obj) {
    //         try {
    //             System.out.println("product-1");
    //             obj.wait();
    //             System.out.println("执行结束");
    //             System.out.println("product-2");
    //         } catch (InterruptedException e) {
    //             e.printStackTrace();
    //         }
    //     }
    // }
    //
    // private static void consumer() {
    //     synchronized (obj) {
    //         System.out.println("consumer-1");
    //         obj.notify();
    //         System.out.println("consumer-2");
    //     }
    // }


}
