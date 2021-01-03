package com.example.william.my.core.network.retrofit.download.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.william.my.core.network.retrofit.download.database.DownloadDataBase;
import com.example.william.my.core.network.retrofit.download.database.api.DownloadRoomApi;
import com.example.william.my.core.network.retrofit.download.database.table.DownloadTask;

import java.util.List;

public class DownloadViewModel extends AndroidViewModel {

    private final DownloadDataBase mDownloadDataBase;

    private MutableLiveData<DownloadTask> download;
    private MutableLiveData<List<DownloadTask>> downloads;

    public DownloadViewModel(@NonNull Application application) {
        super(application);
        mDownloadDataBase = DownloadRoomApi.getInstance().getDownloadDataBase();
    }

    public LiveData<List<DownloadTask>> getDownloads() {
        return mDownloadDataBase.getDownloadDao().getDownloadTaskList();
    }

    public LiveData<DownloadTask> getDownload(String url) {
        return mDownloadDataBase.getDownloadDao().getDownloadTask(url);
    }
}
