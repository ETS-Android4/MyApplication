package com.example.william.my.module.demo;

public class SleepWaitTest {

    public static void main(String[] args) {
        new Thread(new WaitThread()).start();
        new Thread(new SleepThread()).start();
    }

    static class WaitThread implements Runnable {

        @Override
        public void run() {
            synchronized (SleepWaitTest.class) {
                System.out.println("WaitThread.run()");
                try {
                    System.out.println("WaitThread.wait()");
                    SleepWaitTest.class.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("WaitThread end");
            }
        }
    }

    static class SleepThread implements Runnable {

        @Override
        public void run() {
            synchronized (SleepWaitTest.class) {
                System.out.println("SleepThread.run()");
                SleepWaitTest.class.notify();
                try {
                    System.out.println("SleepThread.sleep()");
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("SleepThread end");
            }
        }
    }
}