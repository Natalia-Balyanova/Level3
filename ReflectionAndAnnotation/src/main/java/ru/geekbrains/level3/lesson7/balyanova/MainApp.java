package ru.geekbrains.level3.lesson7.balyanova;

public class MainApp {
    public static void main(String[] args) {
        ClassCanDoTest classCanDoTest = new ClassCanDoTest();
        ClassStartTest.start(classCanDoTest.getClass());
        ClassStartTest.sort(classCanDoTest.getClass());
    }

}