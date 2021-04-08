package ru.geekbrains.level3.lesson5.balyanova;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class Car implements Runnable {
    private static boolean isWinner = false;
    private static int CARS_COUNT;
    private static final CountDownLatch countDownLatchStart;
    private static final CyclicBarrier cyclicBarrier;
    private static final CountDownLatch countDownLatchFinish;

    private final Race race;
    private final int speed;
    private final String name;

    static {//статический блок
        CARS_COUNT = 0;
        countDownLatchStart = MainClass.countDownLatchStart;//обращаемся к классу MainClass, где инициализирована коллекция
        cyclicBarrier = MainClass.cycleBarrier;
        countDownLatchFinish = MainClass.countDownLatchFinish;
    }

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
            countDownLatchStart.countDown();//уменьшаем счетчик
            System.out.println(this.name + " готов");
            //Thread.sleep(500 + (int) (Math.random() * 800));//закомментила т.к. по условию не поняла нужен ли второй рандом
            cyclicBarrier.await();//Потоки закончили подготовку в разное время, но стартовали вместе,
            // так как блокировка снимается одновременно.

        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int j = 0; j < race.getStages().size(); j++) {
            race.getStages().get(j).go(this);
        }
        checkWin(this);//вызываем после того как участники начали последний этап гонки

        countDownLatchFinish.countDown();//как только задача выполнена, уменьшаем счетчик
        //Как только счетчик доходит до нуля, с ожидающего потока снимается блокировка
    }

    public static synchronized void checkWin(Car c) { //метод синхронизирован, иначе несколько машин могут быть победителями
        if(!isWinner) {//если не false
            isWinner = true;//значит машина true. Следующая машина уже не пройдет проверку
            System.out.println(c.name + " WIN");//сразу печатаем результат
        }
    }


}
