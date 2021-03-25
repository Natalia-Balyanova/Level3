package ru.geeekbrains.balyanova.level3.lesson1.hw;

public abstract class Fruit {
    String name;
    float weight;

    public Fruit(float weight) {
        this.weight = weight;
        this.name = name;
    }

    public float getWeight() {
        return weight;
    }
}
