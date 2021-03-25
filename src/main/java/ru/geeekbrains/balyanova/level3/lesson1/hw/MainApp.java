package ru.geeekbrains.balyanova.level3.lesson1.hw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainApp {
    public static void main(String[] args) {
        //1
        String[] arr = {"A", "B", "C", "D"};
        swap(arr);
        System.out.println(Arrays.toString(arr));

        //2
        String[] array = {"1", "2", "3", "4"};
        List<String> listOfStrings = convert(array);
        System.out.println(listOfStrings.getClass() + " " + listOfStrings);

        //3
        Apple apple = new Apple();
        Orange orange = new Orange();

        Box<Apple> appleBox = new Box<>();
        Box<Orange> orangeBox = new Box<>();

        appleBox.add(apple, apple, apple, apple, apple, apple, apple, apple, apple, apple);//10
        orangeBox.add(orange, orange, orange, orange, orange, orange);//9
        orangeBox.add(orange);//не добавится, т.к вес будет больше максимального

        System.out.println(appleBox.compare(orangeBox));//false

        Box<Orange> orangeBox2 = new Box<>();
        orangeBox.sendFruit(orangeBox2);
    }

    public static <E> List<E> convert(E[] array) {
        return Arrays.asList(array);
    }

    public static <T> void swap(T[] arr) {//меняет два элемента массива местами (1 и 4 эл-ты)
        T temp = arr[0];
        arr[0] = arr[3];
        arr[3] = temp;
    }
}