package com.weng.gulimall.product;

// import jdk.internal.vm.annotation.Contended;

public class shardTest {

    public static void main(String[] args) throws InterruptedException {
        testPointer(new Pointer());
    }

    private static void testPointer(Pointer pointer) throws InterruptedException {
        long start = System.currentTimeMillis();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100000000; i++) {
                pointer.a++;
            }
        }, "A");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100000000; i++) {
                pointer.b++;
            }
        },"B");

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println(System.currentTimeMillis() - start);
        System.out.println(pointer.a + "@" + Thread.currentThread().getName());
        System.out.println(pointer.b + "@" + Thread.currentThread().getName());
    }
}

// @Contended
class Pointer {
    //在一个缓存行中，如果有一个线程在读取a时，会顺带把b带出
     long a;  //需要volatile，保证线程间可见并避免重排序
    //    放开下面这行，解决伪共享的问题，提高了性能
 // long p1, p2, p3, p4, p5, p6, p7;
     long b;   //需要volatile，保证线程间可见并避免重排序
}
