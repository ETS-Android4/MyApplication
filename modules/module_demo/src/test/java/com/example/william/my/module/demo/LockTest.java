package com.example.william.my.module.demo;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest {

    private static final Lock lock = new ReentrantLock();
    private static final Condition condition = lock.newCondition();

    public static void main(String[] args) {
        //LockRunnable runnable = new LockRunnable();
        //for (int i = 0; i < 2; i++) {
        //    new Thread(runnable).start();
        //}

        new Thread(new ConditionRunnable1()).start();
        new Thread(new ConditionRunnable2()).start();
    }

    static class LockRunnable implements Runnable {

        @Override
        public void run() {
            lock.lock();
            for (int i = 0; i < 3; i++) {
                System.out.println("func: " + i + "...start...");
            }
            lock.unlock();
        }
    }

    static class ConditionRunnable1 implements Runnable {

        @Override
        public void run() {
            try {
                //上锁
                System.out.println("ConditionRunnable1: " + "lock.lock()");
                lock.lock();
                //线程等待
                System.out.println("ConditionRunnable1: " + "condition.await()");
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("ConditionRunnable1: " + "lock.unlock()");
                lock.unlock();
            }
        }
    }

    static class ConditionRunnable2 implements Runnable {

        @Override
        public void run() {
            try {
                //上锁
                lock.lock();
                System.out.println("ConditionRunnable2: " + "lock.lock()");
                //线程等待通知
                System.out.println("ConditionRunnable2: " + "condition.signal()");
                condition.signal();
            } finally {
                System.out.println("ConditionRunnable2: " + "lock.unlock()");
                lock.unlock();
            }
        }
    }
}
