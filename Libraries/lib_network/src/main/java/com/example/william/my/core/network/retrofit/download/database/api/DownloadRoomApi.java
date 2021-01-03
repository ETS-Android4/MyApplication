package com.example.william.my.core.network.retrofit.download.database.api;

import com.example.william.my.core.network.base.RxRetrofitConfig;
import com.example.william.my.core.network.retrofit.download.database.DownloadDataBase;
import com.example.william.my.core.network.retrofit.download.database.dao.DownloadDao;
import com.example.william.my.core.network.retrofit.download.database.table.DownloadTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DownloadRoomApi {

    private static DownloadRoomApi api;

    private final DownloadDao mDownloadDao;
    private final DownloadDataBase mDownloadDataBase;

    private static CompositeDisposable mDisposable;
    private final ExecutorService mExecutor;

    public DownloadRoomApi() {
        mDownloadDataBase = DownloadDataBase.getInstance(RxRetrofitConfig.getApp());
        mDownloadDao = mDownloadDataBase.getDownloadDao();

        mDisposable = new CompositeDisposable();
        mExecutor = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
    }

    public static DownloadRoomApi getInstance() {
        if (api == null) {
            api = new DownloadRoomApi();
        }
        return api;
    }

    public DownloadDataBase getDownloadDataBase() {
        return mDownloadDataBase;
    }

    public void update(final DownloadTask downloadTask) {
        mDisposable.add(Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mDownloadDao.insertDownloadTask(downloadTask);
            }
        })
                .subscribeOn(Schedulers.from(mExecutor))
                .subscribe());
    }

    public void delete(final String url) {
        mDisposable.add(Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mDownloadDao.deleteDownloadTask(url);
            }
        })
                .subscribeOn(Schedulers.from(mExecutor))
                .subscribe());
    }

    public void deleteAllDownloadTask() {
        mDisposable.add(Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mDownloadDao.deleteAllDownloadTask();
            }
        })
                .subscribeOn(Schedulers.from(mExecutor))
                .subscribe());
    }

    public static void exit() {
        mDisposable.clear();
        DownloadDataBase.exit();
        api = null;
    }
}
