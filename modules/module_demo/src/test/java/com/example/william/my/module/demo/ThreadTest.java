package com.example.william.my.module.demo;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class ThreadTest {

    public static void main(String[] args) {
        new MyThread().start();
        new Thread(new MyRunnable()).start();
        new Thread(new MyFutureTask(new MyCallable())).start();
    }

    static class MyThread extends Thread {

        @Override
        public void run() {
            System.out.println("MyThread.run()");
        }
    }

    static class MyRunnable implements Runnable {

        @Override
        public void run() {
            System.out.println("MyRunnable.run()");
        }
    }

    static class MyFutureTask extends FutureTask<Integer> {

        public MyFutureTask(Callable<Integer> callable) {
            super(callable);
        }

        @Override
        public void run() {
            super.run();
            System.out.println("MyFutureTask.run()");
        }
    }

    static class MyCallable implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            System.out.println("MyCallable.call()");
            return 100;
        }
    }
}

