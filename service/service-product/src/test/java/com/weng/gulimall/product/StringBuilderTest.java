package com.weng.gulimall.product;

import java.util.concurrent.TimeUnit;

public class StringBuilderTest {
    public static void main(String[] args) throws InterruptedException {
        VolatileExample a = new VolatileExample();


        Thread aa = new Thread(() -> {
            int b = 0;
            while (a.x){
                if (b++>0) System.out.println(a.p1 = false);
            }
        },"aa");


        Thread a1 = new Thread(() -> {



        },"a1");


        Thread a2 = new Thread(() -> {

        },"a2");
        aa.start();

        a1.start();
        a2.start();

        a1.join();
        a2.join();


        // Thread thread = new Thread(() -> {
        //     System.out.println(2);
        // });
        // thread.join();
        // System.out.println(2);
    }
}

class VolatileExample {
    // NacosTest x = new NacosTest();
    // NacosTest p1 = new NacosTest();
   boolean x = true;

    long p2 = 2;
    long p3 = 2;
    long p4 = 2;
    long p5 = 2;
    long p6 = 2;
    long p7 = 2;
    boolean p1 = true;
    long x1 = 2;
    long x3 = 2;
    long x4 = 2;
    long x5 = 2;
    long x6 = 2;
    long x7 = 2;
    volatile boolean v = false;

    public void writer() throws InterruptedException {

    }

    public void reader() throws InterruptedException {
        while (p1){

        }
    }
}