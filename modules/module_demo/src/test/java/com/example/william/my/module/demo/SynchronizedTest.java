package com.example.william.my.module.demo;

public class SynchronizedTest {

    public static void main(String[] args) {

        for (int i = 0; i < 3; i++) {
            int key = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    new Sync1().func(key);
                }
            }).start();
        }

        for (int i = 0; i < 3; i++) {
            int key = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Sync2.func(key);
                }
            }).start();
        }

        for (int i = 0; i < 3; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    new Sync3().func1();
                }
            }).start();
        }

        for (int i = 0; i < 3; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    new Sync3().func2();
                }
            }).start();
        }

        Sync3 sync = new Sync3();
        for (int i = 0; i < 3; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //sync.func3();
                    sync.func4();
                }
            }).start();
        }
    }

    /**
     * 修饰非静态方法，锁定的是调用对象
     */
    static class Sync1 {

        public synchronized void func(int key) {
            System.out.println("func: " + key + "...start...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("func: " + key + "...end...");
        }
    }

    /**
     * 修饰静态方法，锁定的是调用类
     */
    static class Sync2 {

        public static synchronized void func(int key) {
            System.out.println("Sync2: " + key + "...start...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Sync2: " + key + "...end...");
        }
    }

    /**
     * 修饰代码块，锁定的是传入的对象
     */
    static class Sync3 {

        public void func1() {
            synchronized (this) {
                System.out.println("Sync3: " + "this" + "...start...");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Sync3: " + "this" + "...end...");
            }
        }

        public void func2() {
            synchronized (Sync3.class) {
                System.out.println("Sync3: " + "class" + "...start...");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Sync3: " + "class" + "...end...");
            }
        }

        private Integer key = 0;

        public void func3() {
            /*
             * Synchronization on a non-final field 'key'
             */
            synchronized (key) {
                key++;
                System.out.println("Sync3: " + key + "...start...");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private final Integer[] key2 = new Integer[]{0};

        public void func4() {
            synchronized (key2) {
                key2[0] = key2[0] + 1;
                System.out.println("Sync3: " + key2[0] + "...start...");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
