package com.weng.gulimall.product;

public class QuickSort {

        public static void quickSort(int[] data, int low, int high) {
            int i, j, temp, t;
            if (low > high) {
                return;
            }
            i = low;
            j = high;
            //temp就是基准位
            temp = data[low];
            System.out.println("基准位：" + temp);

            while (i < j) {
                //先看右边，依次往左递减
                while (temp <= data[j] && i < j) {
                    j--;
                }
                //再看左边，依次往右递增
                while (temp >= data[i] && i < j) {
                    i++;
                }
                //如果满足条件则交换
                if (i < j) {
                    System.out.println("交换：" + data[i] + "和" + data[j]);
                    t = data[j];
                    data[j] = data[i];
                    data[i] = t;
                    System.out.println(java.util.Arrays.toString(data));

                }
            }
            //最后将基准位与i和j相等位置的数字交换
            System.out.println("基准位" + temp + "和i、j相遇的位置" + data[i] + "交换");
            data[low] = data[i];
            data[i] = temp;
            System.out.println(java.util.Arrays.toString(data));

            //递归调用左半数组
            quickSort(data, low, j - 1);
            //递归调用右半数组
            quickSort(data, j + 1, high);
        }


        public static void main(String[] args) {

            int[] data = {3, 44, 38, 5, 47, 15, 36, 26, 27, 2, 46, 4, 19, 50, 48};

            System.out.println("排序之前：\n" + java.util.Arrays.toString(data));

            quickSort(data, 0, data.length - 1);

            System.out.println("排序之后：\n" + java.util.Arrays.toString(data));
        }
    }

