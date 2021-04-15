package ru.geekbrains.balyanova.testing;

import java.util.Arrays;


public class ArrayMain {

    public static void main(String[] args) {
        //1
        int[] arr = {1, 2, 4, 4, 2, 3, 4, 1, 7};
        System.out.println(Arrays.toString(methodReturn(arr)));//[1, 7]

        //2
        int[] array = {1, 1, 1, 4, 4, 1, 4, 4};
        System.out.println(isArrayEqualsOneAndFour(array));//true

    }

    public static int[] methodReturn (int[] arr) {
        int[] newArray = null;
        int x = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 4) {
                x = arr[i];
                newArray = new int[arr.length - i - 1];
                for (int j = i + 1, k = 0; j < arr.length; j++, k++) {
                    if (arr[j] == 4) {
                        continue;
                    }
                    newArray[k] = arr[j];
                }
            }
        }
        try {
            x = 1 / x;
        } catch (ArithmeticException e) {
            throw new RuntimeException();
        }
        return newArray;
    }

    public static boolean isArrayEqualsOneAndFour(int[] array) {
        for (int i = 0; i < array.length; i++) {
            if(array[i] != 1 && array[i] != 4) {
                return false;
            }
        }
        return true;
    }
}
