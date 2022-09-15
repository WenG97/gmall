package com.weng.gulimall.product;

import java.util.HashMap;
import java.util.Map;

public class sumTest {

    public static void main(String[] args) {
        int[] ints = {1, 5, 6, 9, 8, 23, 5, 86, 4, 86, 63, 58,};
        int[] ints1 = twoSum(ints, 9);
        for (int i = 0; i < ints1.length; i++) {
            System.out.println(ints1[i]);
        }
    }

    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (int i = 0; i < nums.length; i++) {//只遍历一次数组
            if (map.containsKey(target - nums[i])) {//在集合中找符合条件的元素
                return new int[] { map.get(target - nums[i]), i };
            }
            map.put(nums[i], i);//将数组中的元素存入集合
        }
        throw new IllegalArgumentException("No two sum solution");
    }
}
