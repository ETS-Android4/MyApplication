package com.example.william.my.core.network.retrofit.download.observer;

import android.os.Handler;
import android.util.Log;

import com.example.william.my.core.network.retrofit.download.DownloadUtils;
import com.example.william.my.core.network.retrofit.download.callback.DownloadCallback;
import com.example.william.my.core.network.retrofit.download.database.api.DownloadRoomApi;
import com.example.william.my.core.network.retrofit.download.database.table.DownloadTask;
import com.example.william.my.core.network.retrofit.download.state.DownloadState;
import com.example.william.my.core.network.retrofit.exception.ApiException;
import com.example.william.my.core.network.retrofit.exception.ExceptionHandler;
import com.example.william.my.core.network.retrofit.listener.RetrofitResponseListener;

import java.lang.ref.SoftReference;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class DownloadObserver implements Observer<DownloadTask>, RetrofitResponseListener {

    private static final String TAG = "DownloadObserver";

    private Disposable disposable;

    private final Handler handler;
    private final DownloadTask downloadTask;
    private final SoftReference<DownloadCallback> downloadCallback;

    public DownloadObserver(DownloadTask downloadTask, Handler handler) {
        this.handler = handler;
        this.downloadTask = downloadTask;
        this.downloadCallback = new SoftReference<>(downloadTask.getCallback());
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        disposable = d;

        downloadTask.setState(DownloadState.WAITING.getValue());//等待状态

        Log.e(TAG, "等待下载：" + downloadTask.getDownloadUrl());

        // 下载进度写入数据库
        DownloadRoomApi.getInstance().update(downloadTask);
    }

    @Override
    public void onNext(@NonNull DownloadTask downloadTask) {

        downloadTask.setState(DownloadState.FINISH.getValue());//完成状态

        Log.e(TAG, "下载完成：" + downloadTask.getDownloadUrl());

        DownloadUtils.getInstance().remove(downloadTask);//移除下载

        if (downloadCallback.get() != null) {//回调
            downloadCallback.get().onResponse(downloadTask);
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {

        downloadTask.setState(DownloadState.ERROR.getValue());//错误状态

        Log.e(TAG, "下载错误：" + downloadTask.getDownloadUrl());

        DownloadUtils.getInstance().remove(downloadTask);//移除下载

        if (downloadCallback.get() != null) {
            downloadCallback.get().onProgress(downloadTask.getDownloadUrl(), downloadTask.getState(), downloadTask.getCurrentSize(), downloadTask.getTotalSize());

            if (e instanceof ApiException) {
                downloadCallback.get().onFailure((ApiException) e);
            } else {
                downloadCallback.get().onFailure(new ApiException(e, ExceptionHandler.ERROR.UNKNOWN));
            }
        }

        dispose();
    }

    @Override
    public void onComplete() {
        dispose();
    }

    @Override
    public void onProgress(final String url, long currentSize, long totalLength) {

        //final int progress = (int) (currentSize * 1f / totalLength * 100);
        //Log.e(TAG, "下载进度：" + progress + "%");

        //Log.e(TAG, "download.getTotalLength()  " + downloadTask.getTotalLength());
        //Log.e(TAG, "totalLength  " + totalLength);

//        if (download.getTotalLength() > totalLength) {
//            currentSize = download.getTotalSize() - totalSize + currentSize;
//        } else {
//            download.setTotalLength(totalLength);
//        }

        downloadTask.setTotalSize(totalLength);
        downloadTask.setCurrentSize(currentSize);

        handler.post(new Runnable() {
            @Override
            public void run() {
                /*
                 * 下载进度==总进度修改为完成状态
                 */
                if ((downloadTask.getCurrentSize() == downloadTask.getTotalSize()) && (downloadTask.getTotalSize() != 0)) {
                    downloadTask.setState(DownloadState.FINISH.getValue());
                }
                if (downloadTask.getState() != DownloadState.STOP.getValue()) {
                    if (downloadCallback.get() != null) {
                        downloadCallback.get().onProgress(url, downloadTask.getState(), downloadTask.getCurrentSize(), downloadTask.getTotalSize());
                    }
                }

                // 下载进度写入数据库
                DownloadRoomApi.getInstance().update(downloadTask);

            }
        });
    }

    public void dispose() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}