package com.example.william.my.core.network.retrofit.download;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.william.my.core.network.retrofit.api.Api;
import com.example.william.my.core.network.retrofit.download.database.api.DownloadRoomApi;
import com.example.william.my.core.network.retrofit.download.database.table.DownloadTask;
import com.example.william.my.core.network.retrofit.download.function.DownloadFunction;
import com.example.william.my.core.network.retrofit.download.observer.DownloadObserver;
import com.example.william.my.core.network.retrofit.download.state.DownloadState;
import com.example.william.my.core.network.retrofit.interceptor.RetrofitInterceptorProgress;
import com.example.william.my.core.network.retrofit.utils.RetrofitUtils;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DownloadUtils {

    private static final String TAG = "DownloadUtils";

    private final Handler mHandler;
    private final ExecutorService mExecutor;
    private final HashMap<String, DownloadObserver> mCallMap;

    private DownloadUtils() {
        mCallMap = new HashMap<>();

        mHandler = new Handler(Looper.getMainLooper());

        mExecutor = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
    }

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

    public void enqueue(final DownloadTask downloadTask) {
        if (downloadTask == null) return;

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

        RetrofitInterceptorProgress interceptor = new RetrofitInterceptorProgress(observer);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitUtils.getBasUrl(downloadTask.getDownloadUrl()))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(okHttpClient).build();

        retrofit.create(Api.class)
                .downloadFile("0", downloadTask.getDownloadUrl())
                .map(new DownloadFunction(downloadTask))
                .subscribeOn(Schedulers.from(mExecutor))
                //.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

        //插入数据库
        DownloadRoomApi.getInstance().update(downloadTask);
    }

    private void stop(DownloadTask downloadTask) {
        if (downloadTask == null) return;

        if (mCallMap.containsKey(downloadTask.getDownloadUrl())) {
            DownloadObserver observer = mCallMap.get(downloadTask.getDownloadUrl());
            if (observer != null)
                observer.dispose();//取消
            mCallMap.remove(downloadTask.getDownloadUrl());
        }

        //暂停状态
        downloadTask.setState(DownloadState.STOP.getValue());

        if (downloadTask.getCallback() != null) {
            downloadTask.getCallback().onProgress(
                    downloadTask.getDownloadUrl(), downloadTask.getState(), downloadTask.getCurrentSize(), downloadTask.getTotalSize());
        }

        //更新数据库
        DownloadRoomApi.getInstance().update(downloadTask);
    }

    public void remove(DownloadTask downloadTask) {
        if (downloadTask == null) return;

        if (downloadTask.getState() != DownloadState.FINISH.getValue()) {
            stop(downloadTask);
        }

        mCallMap.remove(downloadTask.getDownloadUrl());

        //更新数据库
        DownloadRoomApi.getInstance().delete(downloadTask.getDownloadUrl());
    }


    public HashMap<String, DownloadObserver> getDownloadCallMap() {
        if (mCallMap == null) {
            return new HashMap<>();
        }
        return mCallMap;
    }
}
