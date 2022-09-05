package com.weng.gulimall.product;

public class StringBuilderTest {
    public static void main(String[] args) {

        StringBuilder a = new StringBuilder();
        a.append("aa");
        a.append("bb");
        System.out.println("a = " + a);
        aa(a);
        System.out.println("a = " + a);
    }

    private static void aa (StringBuilder bb){

        bb.append("cc");
    }

}
