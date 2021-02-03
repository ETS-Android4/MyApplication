package com.example.william.my.module.sample;

/**
 * 常用排序算法
 */
public class SortTest {

    /**
     * 插入排序算法
     *
     * @param array 未排序数组
     * @return 排完序数组
     */
    public int[] sortInsert(int[] array) {
        for (int i = 1; i < array.length; i++) {
            int temp = array[i];
            int j;
            for (j = i - 1; j >= 0 && temp < array[j]; j--) {
                array[j + 1] = array[j];
            }
            array[j + 1] = temp;
        }
        return array;
    }

    /**
     * 选择排序算法
     *
     * @param array 未排序数组
     * @return 排完序数组
     */
    public int[] sortSelect(int[] array) {
        for (int i = 0; i < array.length; i++) {
            int miniPost = i;
            for (int m = i + 1; m < array.length; m++) {
                if (array[m] < array[miniPost]) {
                    miniPost = m;
                }
            }

            if (array[i] > array[miniPost]) {
                int temp;
                temp = array[i];
                array[i] = array[miniPost];
                array[miniPost] = temp;
            }
        }
        return array;
    }

    /**
     * 冒泡排序算法
     *
     * @param array 未排序数组
     * @return 排完序数组
     */
    public int[] sortBubble(int[] array) {
        int temp;
        // 第一层循环:表明比较的次数, 比如 length 个元素,比较次数为 length-1 次（肯定不需和自己比）
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = array.length - 1; j > i; j--) {
                if (array[j] < array[j - 1]) {
                    temp = array[j];
                    array[j] = array[j - 1];
                    array[j - 1] = temp;
                }
            }
        }
        return array;
    }
}