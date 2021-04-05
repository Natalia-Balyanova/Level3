package ru.geekbrains.balyanova.marchchat;

public class WaitNotifyApp {
    
    private char currentLetter = 'A';
    private final Object monitor = new Object();

    public static void main(String[] args) {//notifyAll когда мы не знаем в каком порядке потоки пришли в wait

        WaitNotifyApp waitNotifyApp = new WaitNotifyApp();//в каждом методе идет синхронизация по монитору
        //после чего говорим, что надо напечатать какую-то из букв
        //перед тем как печатать проверяем, чья сейчас очередь, если не A, то ждем (печатаем B) и наоборот И Т.Д
        new Thread(waitNotifyApp::printA).start();
        new Thread(waitNotifyApp::printB).start();
        new Thread(waitNotifyApp::printC).start();
        new Thread(waitNotifyApp::printD).start();
    }

    private void printA() {
        synchronized (monitor) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (currentLetter != 'A') {
                        monitor.wait();
                    }
                    System.out.print("A");
                    currentLetter = 'B';
                    monitor.notifyAll();
                    Thread.sleep(1000);
                }
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void printB() {
        synchronized (monitor) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (currentLetter != 'B') {
                        monitor.wait();
                    }
                    System.out.print("B");
                    currentLetter = 'C';
                    monitor.notifyAll();
                    Thread.sleep(1);
                }
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void printC() {
        synchronized (monitor) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (currentLetter != 'C') {
                        monitor.wait();
                    }
                    System.out.print("C");
                    currentLetter = 'D';
                    monitor.notifyAll();
                    Thread.sleep(10);
                }
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void printD() {
        synchronized (monitor) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (currentLetter != 'D') {
                        monitor.wait();
                    }
                    System.out.print("D");
                    currentLetter = 'A';
                    monitor.notifyAll();
                    Thread.sleep(100);
                }
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
