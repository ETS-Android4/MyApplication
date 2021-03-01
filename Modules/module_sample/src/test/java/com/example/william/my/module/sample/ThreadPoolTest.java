package com.example.william.my.module.sample;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 线程池
 */
public class ThreadPoolTest {

    // 单线程线程池
    ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();

    // 定长线程池
    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);

    // 缓存线程池
    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    // 定长线程池 定时及周期性任务执行，延迟执行
    ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
}
