package com.example.william.my.core.download;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.william.my.core.download.observer.DownloadObserver;
import com.example.william.my.core.download.state.DownloadState;
import com.example.william.my.core.download.task.DownloadTask;

import java.util.HashMap;

public class DownloadUtils {

    private static final String TAG = "DownloadUtils";

    private final Handler mHandler;
    private final HashMap<String, DownloadObserver> mCallMap;

    private volatile static DownloadUtils mInstance;

    public static DownloadUtils getInstance() {
        if (mInstance == null) {
            synchronized (DownloadUtils.class) {
                if (mInstance == null) {
                    mInstance = new DownloadUtils();
                }
            }
        }
        return mInstance;
    }

    private DownloadUtils() {
        mCallMap = new HashMap<>();

        mHandler = new Handler(Looper.getMainLooper());
    }

    public void enqueue(DownloadTask downloadTask) {
        /*正在下载不处理*/
        if (mCallMap.get(downloadTask.getDownloadUrl()) != null) {
            Log.e(TAG, "任务下载中：" + downloadTask.getDownloadUrl());
            return;
        }

        //if (downloadTask.getCurrentSize() == downloadTask.getTotalSize() && (downloadTask.getTotalSize() != 0)) {
        //    return;
        //}

        DownloadObserver observer = new DownloadObserver(downloadTask, mHandler);
        mCallMap.put(downloadTask.getDownloadUrl(), observer);
    }

    private void stop(DownloadTask downloadTask) {
        if (mCallMap.containsKey(downloadTask.getDownloadUrl())) {
            DownloadObserver observer = mCallMap.get(downloadTask.getDownloadUrl());
            if (observer != null) {
                observer.dispose();//取消
            }
        }

        //if (downloadTask.getDownloadCallback() != null) {
        //    downloadTask.setState(DownloadState.STOP.getValue());
        //    downloadTask.getDownloadCallback().onProgress(downloadTask);
        //}
    }

    public void remove(DownloadTask downloadTask) {
        if (downloadTask.getState() != DownloadState.FINISH.getValue()) {
            stop(downloadTask);
        }
        mCallMap.remove(downloadTask.getDownloadUrl());
    }
}
