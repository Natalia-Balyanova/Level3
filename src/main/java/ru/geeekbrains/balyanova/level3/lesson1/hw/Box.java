package ru.geeekbrains.balyanova.level3.lesson1.hw;

import java.util.ArrayList;
import java.util.Arrays;

public class Box<T extends Fruit> {
    private ArrayList<T> fruitList;
    private final float maxValueBox = 10.0f;
    private float weight = 0.0f;

    public Box(T... items) {
        this.fruitList = new ArrayList<T>(Arrays.asList(items));
    }

    public float getWeight() {//высчитывает вес коробки, зная вес одного фрукта и их количество
        for (T item: fruitList) {
            weight += item.getWeight();
        }
        return weight;
    }

    public void add(T... fruit) {
        while (getWeight() + 1 <= maxValueBox) {//чтобы нельзя было бесконечно cкладывать фрукты, но работает олько при добавлении, а не при инициализации
            this.fruitList.addAll(Arrays.asList(fruit));
            System.out.println("В коробку положили фрукт. Вес коробки с фруктами: " + getWeight());
        } if (fruitList.size() > maxValueBox) {
                System.out.println("Коробка переполнена. Невозможно положить фрукт");
            }
    }

    public boolean compare(Box box) {//сравниваем размер коробок, в т.ч разных типов
        return this.getWeight() == box.getWeight();
    }

    public void sendFruit(Box<? super T> box) { //можно перекладывать фрукты только одного типа
        box.fruitList.addAll(this.fruitList);//перекидываются объекты, которые были в первой коробке
        System.out.println("Переложили фрукты из коробки");
        box.clear();//в текущей коробке фруктов не остается
    }

    public void clear() {//очистить коробку
        fruitList.clear();
        System.out.println("Очистили коробку");
    }
}