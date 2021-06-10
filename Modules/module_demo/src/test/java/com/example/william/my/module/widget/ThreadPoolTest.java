package com.example.william.my.module.widget;

import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 线程池
 * LinkedBlockingQueue，无界缓存的等待队列
 * SynchronousQueue，无缓冲的等待队列
 */
public class ThreadPoolTest {

    private static final String TAG = ThreadPoolTest.class.getSimpleName();

    // 单线程线程池
    ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();

    // 定长线程池
    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);

    // 缓存线程池
    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    // 定时线程池 定时及周期性任务执行，延迟执行
    ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);

    /**
     * thread1 enter...
     * thread1 is waiting...
     * sleep 5000
     * thread2 enter....
     * thread2 is sleep....
     * sleep 5000
     * thread2 is going on....
     * thread2 is over!!!
     * thread1 is going on ....
     * thread1 is over!!!
     */
    public static void sleepAndWait() {
        new Thread(new ThreadWait()).start();
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Thread(new ThreadSleep()).start();
    }


    private static class ThreadWait implements Runnable {
        @Override
        public void run() {
            synchronized (ThreadPoolTest.class) {
                Log.e(TAG, "thread1 enter...");
                try {
                    Log.e(TAG, "thread1 is waiting...");
                    //调用wait()方法，线程会放弃对象锁，进入等待此对象的等待锁定池
                    ThreadPoolTest.class.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "thread1 is going on ....");
                Log.e(TAG, "thread1 is over!!!");
            }
        }
    }

    private static class ThreadSleep implements Runnable {
        @Override
        public void run() {
            synchronized (ThreadPoolTest.class) {
                Log.e(TAG, "thread2 enter....");
                //只有针对此对象调用notify()方法后本线程才进入对象锁定池准备获取对象锁进入运行状态。
                ThreadPoolTest.class.notify();
                //==================
                //区别
                //如果我们把代码：TestD.class.notify();给注释掉，即TestD.class调用了wait()方法，但是没有调用notify()
                //方法，则线程永远处于挂起状态。
                try {
                    Log.e(TAG, "thread2 is sleep....");
                    //sleep()方法导致了程序暂停执行指定的时间，让出cpu该其他线程，
                    //但是他的监控状态依然保持者，当指定的时间到了又会自动恢复运行状态。
                    //在调用sleep()方法的过程中，线程不会释放对象锁。
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "thread2 is going on....");
                Log.e(TAG, "thread2 is over!!!");
            }
        }
    }

}
