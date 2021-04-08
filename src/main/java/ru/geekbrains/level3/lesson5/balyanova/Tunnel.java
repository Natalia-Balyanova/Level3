package ru.geekbrains.level3.lesson5.balyanova;

import java.util.concurrent.Semaphore;

public class Tunnel extends Stage {
    public static Semaphore semaphore = new Semaphore(MainClass.CARS_COUNT / 2);
    //ограничивает количество потоков при работе с ресурсами
    public Tunnel() {
        this.length = 80;
        this.description = "Тоннель " + length + " метров";
    }
    @Override
    public void go(Car c) {
        try {
            try {
                System.out.println(c.getName() + " готовится к этапу(ждет): " + description);
                semaphore.acquire();//получение доступа к ресурсу// одновременно захватить семафор могут только два потока
                System.out.println(c.getName() + " начал этап: " + description);
                Thread.sleep(length / c.getSpeed() * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();//освобождение ресурса одной из машин
                System.out.println(c.getName() + " закончил этап: " + description);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}