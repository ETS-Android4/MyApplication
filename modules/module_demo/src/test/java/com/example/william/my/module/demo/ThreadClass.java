package com.example.william.my.module.demo;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class ThreadClass {

    public static void main(String[] args) {
        new MyThread().start();
        new MyRunnable().run();
        new MyFutureTask(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println("MyCallable.call()");
                return 100;
            }
        }).run();
    }
}


class MyThread extends Thread {

    @Override
    public void run() {
        System.out.println("MyThread.run()");
    }
}

class MyRunnable implements Runnable {

    @Override
    public void run() {
        System.out.println("MyRunnable.run()");
    }
}

class MyFutureTask extends FutureTask<Integer> {

    public MyFutureTask(Callable<Integer> callable) {
        super(callable);
    }

    @Override
    public void run() {
        super.run();
        System.out.println("MyFutureTask.run()");
    }
}