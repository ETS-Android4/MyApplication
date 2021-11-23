package com.example.william.my.module.demo;

@SuppressWarnings("InstantiationOfUtilityClass")
public class Singleton {

    /**
     * 饿汉式
     */
    private static Singleton instance;

    public static Singleton getInstance1() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    /**
     * 线程安全
     */
    private static Singleton instance2;

    public static synchronized Singleton getInstance2() {
        if (instance2 == null) {
            instance2 = new Singleton();
        }
        return instance2;
    }

    private volatile static Singleton instance3;


    /**
     * 双重校验锁
     */
    public static Singleton getInstance3() {
        if (instance3 == null) {
            synchronized (Singleton.class) {
                if (instance3 == null) {
                    instance3 = new Singleton();
                }
            }
        }
        return instance3;
    }

    /**
     * 静态内部类
     */
    private static class SingletonHolder {
        private static final Singleton INSTANCE = new Singleton();
    }

    public static Singleton getInstance4() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 枚举
     */
    enum Singleton5 {
        INSTANCE
    }
}
