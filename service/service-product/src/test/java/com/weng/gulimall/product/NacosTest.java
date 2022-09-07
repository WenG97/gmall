package com.weng.gulimall.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class NacosTest {

    // @Value("${aaa}")
    // String aa;
    //
    // @Test
    // public void test01(){
    //     System.out.println("aa = " + aa);
    // }

   static Object obj = new Object();
   static Object aObj = new Object();

    public static void main(String[] args) {

        new Thread(()->{
            product();
        }).start();


        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            consumer();
        }).start();

        try {
            TimeUnit.SECONDS.sleep(1000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static void product() {
        synchronized (obj) {
            try {
                System.out.println("product-1");
                obj.wait();
                System.out.println("执行结束");
                System.out.println("product-2");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void consumer(){
        synchronized (obj){
            System.out.println("consumer-1");
            obj.notify();
            System.out.println("consumer-2");
        }
    }


}
