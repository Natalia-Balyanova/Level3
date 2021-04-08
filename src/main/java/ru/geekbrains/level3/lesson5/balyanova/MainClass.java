package ru.geekbrains.level3.lesson5.balyanova;

/*
Организуем гонки:
Все участники должны стартовать одновременно, несмотря на разное время  подготовки.
В тоннель не может одновременно заехать больше половины участников (условность).
Попробуйте все это синхронизировать.
Первый участник, пересекший финишную черту, объявляется победителем (в момент пересечения этой самой черты).
Победитель должен быть только один (ситуация с 0 или 2+ победителями недопустима).
Когда все завершат гонку, нужно выдать объявление об окончании.
Можно корректировать классы (в том числе конструктор машин) и добавлять объекты классов из пакета java.util.concurrent.

 */

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class MainClass {
    //можем сделать поля дефолтными, т.к. классы в пакете имеют доступ
    static final int CARS_COUNT = 4;
    static final CyclicBarrier cycleBarrier = new CyclicBarrier(CARS_COUNT);
    static final CountDownLatch countDownLatchStart = new CountDownLatch(CARS_COUNT);
    static final CountDownLatch countDownLatchFinish = new CountDownLatch(CARS_COUNT);

    public static void main(String[] args)  {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10));
        }
        for (int i = 0; i < cars.length; i++) {
            new Thread(cars[i]).start();
        }
        try {
            countDownLatchStart.await();//стоим на месте, ожидаем когда машины подготовятся
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");

        try {
            countDownLatchFinish.await();//стоим на месте, ждем, когда все машину доедут
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}




