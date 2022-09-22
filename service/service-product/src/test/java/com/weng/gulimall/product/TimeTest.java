package com.weng.gulimall.product;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TimeTest {
    public static void main(String[] args) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy MM dd");

        String format = dateTimeFormatter.format(LocalDate.now());

        System.out.println("format = " + format);
    }
}
