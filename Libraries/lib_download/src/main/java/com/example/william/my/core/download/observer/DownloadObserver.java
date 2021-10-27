package com.example.william.my.core.download.observer;

import android.os.Handler;
import android.util.Log;

import com.example.william.my.core.download.DownloadUtils;
import com.example.william.my.core.download.callback.DownloadCallback;
import com.example.william.my.core.download.state.DownloadState;
import com.example.william.my.core.download.task.DownloadTask;

import java.lang.ref.SoftReference;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class DownloadObserver implements Observer<DownloadTask> {

    private final String TAG = this.getClass().getSimpleName();

    private Disposable disposable;

    private final Handler mHandler;
    private final DownloadTask mDownloadTask;
    private final SoftReference<DownloadCallback> mDownloadCallback;

    public DownloadObserver(DownloadTask downloadTask, Handler handler) {
        this.mHandler = handler;
        this.mDownloadTask = downloadTask;
        this.mDownloadCallback = new SoftReference<>(downloadTask.getDownloadCallback());

        /*
         * 下载状态
         */
        mDownloadTask.setState(DownloadState.LOADING.getValue());
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        disposable = d;

        /*
         * 等待状态
         */
        mDownloadTask.setState(DownloadState.WAITING.getValue());
        Log.e(TAG, "等待下载：" + mDownloadTask.getDownloadUrl());
    }

    @Override
    public void onNext(@NonNull DownloadTask downloadTask) {
        /*
         * 完成状态
         */
        mDownloadTask.setState(DownloadState.FINISH.getValue());
        Log.e(TAG, "下载完成：" + downloadTask.getDownloadUrl());

        /*
         * 移除下载
         */
        DownloadUtils.getInstance().remove(mDownloadTask);

        if (mDownloadCallback.get() != null) {
            mDownloadCallback.get().onProgress(mDownloadTask);
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {
        /*
         * 错误状态
         */
        mDownloadTask.setState(DownloadState.ERROR.getValue());
        Log.e(TAG, "下载错误：" + mDownloadTask.getDownloadUrl());

        /*
         * 移除下载
         */
        DownloadUtils.getInstance().remove(mDownloadTask);

        //if (e instanceof ApiException) {
        //    mDownloadCallback.get().onFailure((ApiException) e);
        //} else {
        //    mDownloadCallback.get().onFailure(new ApiException(e, ExceptionHandler.ERROR.UNKNOWN));
        //}

        dispose();
    }

    @Override
    public void onComplete() {
        dispose();
    }

    public void dispose() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}