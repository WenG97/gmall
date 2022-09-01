package com.weng.starter.cache.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

/**
 * 线程安全的，spring自带的fastjson的转换工具
 */
public class Jsons {

    private static ObjectMapper objectMapper = new ObjectMapper();


    /**
     * 带复杂泛型的json转换为复杂泛型的对象，且兼容Class
     * @param str
     * @param tr
     * @param <T>
     * @return
     */
    public static <T> T toObj(String str, TypeReference<T> tr ){
        if (StringUtils.isEmpty(str)){
            return null;
        }
        try {
            T t = objectMapper.readValue(str, tr);
            return t;
        } catch (JsonProcessingException e) {
            return null;
        }
        
    }



    /**
     * 将字符串转为指定的普通对象
     * @param str 要转换的字符串
     * @param <T> 要转换为什么类型的对象
     * @return 返回一个对象
     */
    public static <T> T toObj(String str, Class<T> cla) {

        if (StringUtils.isEmpty(str)){
            return null;
        }
        try {
            T t = objectMapper.readValue(str, cla);
            return t;
        } catch (JsonProcessingException e) {
            return null;
        }
    }


    /**
     * 将对象转为json字符串
     *
     * @param obj 传入的对象
     * @return 返回的字符串
     */
    public static String toStr(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
