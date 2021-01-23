package com.example.william.my.library.helper;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * App全局线程池
 * Global executor pools for the whole application.
 * <p>
 * 避免内存不足影响，例如磁盘读取不会延迟网络请求
 * Grouping tasks like this avoids the effects of task starvation
 * (e.g. disk reads don't wait behind webservice requests).
 */
public class AppExecutorsHelper {

    private static AppExecutorsHelper instance;

    public static AppExecutorsHelper getInstance() {
        if (instance == null) {
            synchronized (AppExecutorsHelper.class) {
                if (instance == null) {
                    instance = new AppExecutorsHelper();
                }
            }
        }
        return instance;
    }

    private AppExecutorsHelper() {
        this(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(3),
                new MainThreadExecutor(), Executors.newScheduledThreadPool(count));
    }

    //磁盘IO线程
    private final Executor mDiskIO;

    //网络IO线程
    private final Executor mNetworkIO;

    //UI线程
    private final Executor mMainThread;

    //定时任务线程池
    private final ScheduledExecutorService scheduledExecutor;

    private static final int count = Runtime.getRuntime().availableProcessors() * 3 + 2;

    private AppExecutorsHelper(Executor diskIO, Executor networkIO, Executor mainThread, ScheduledExecutorService scheduledExecutor) {
        this.mDiskIO = diskIO;
        this.mNetworkIO = networkIO;
        this.mMainThread = mainThread;
        this.scheduledExecutor = scheduledExecutor;
    }

    public Executor diskIO() {
        return mDiskIO;
    }

    public Executor networkIO() {
        return mNetworkIO;
    }

    public Executor mainThread() {
        return mMainThread;
    }

    /**
     * 定时(延时)任务线程池
     * <p>
     * 替代Timer,执行定时任务,延时任务
     */
    public ScheduledExecutorService scheduledExecutor() {
        return scheduledExecutor;
    }


    private static class MainThreadExecutor implements Executor {
        private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
