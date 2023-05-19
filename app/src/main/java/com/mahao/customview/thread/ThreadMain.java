package com.mahao.customview.thread;

import java.util.concurrent.FutureTask;

public class ThreadMain {

    static Object mObject = new Object();

    public static void main(String[] args) {
        ThreadOne threadOne = new ThreadOne();
        ThreadOne theadOne1 = new ThreadOne();
        //   threadOne.start();
        //   theadOne1.start();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                plusNumber();
            }
        };

        Runnable runnable1 = new Runnable() {

            @Override
            public void run() {
                plusNumber();
            }
        };

        for (int j = 0; j < 30; j++) {
            new Thread(runnable).start();
            new Thread(runnable1).start();
        }
    }

    static int i = 200;

    public static void plusNumber() {
        System.out.println(Thread.currentThread().getName() + "====");
        synchronized (mObject) {
            for (; i > 0; i--) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (i <= 2) {
                    System.out.println(Thread.currentThread().getName() + " ------ " + i);
                } else {
                    System.out.println(Thread.currentThread().getName() + " " + i);
                }
            }
        }
    }
}
