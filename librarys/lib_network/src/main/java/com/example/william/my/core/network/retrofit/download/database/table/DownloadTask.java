package com.example.william.my.core.network.retrofit.download.database.table;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.william.my.core.network.retrofit.download.callback.DownloadCallback;
import com.example.william.my.core.network.retrofit.download.state.DownloadState;

import java.io.Serializable;

@Entity(tableName = "download")
public class DownloadTask implements Serializable {

    @NonNull
    @PrimaryKey()
    @ColumnInfo(name = "url")
    private String downloadUrl;

    @ColumnInfo(name = "name")
    private String downloadFileName;

    @ColumnInfo(name = "path")
    private String downloadFilePath;

    @ColumnInfo(name = "current")
    private long currentSize;//当前大小
    @ColumnInfo(name = "total")
    private long totalSize;//文件大小

    @ColumnInfo(name = "state")
    private int state = DownloadState.NONE.getValue();

    @Ignore
    private DownloadCallback callback;

    public DownloadTask(@NonNull String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public DownloadTask(String url, String name) {
        setDownloadUrl(url);
        setDownloadFileName(name);
    }

    public DownloadTask(String url, String name, DownloadCallback callback) {
        setDownloadUrl(url);
        setDownloadFileName(name);

        setCallback(callback);
    }

    @NonNull
    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(@NonNull String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getDownloadFileName() {
        return downloadFileName == null ? "" : downloadFileName;
    }

    public void setDownloadFileName(String downloadFileName) {
        this.downloadFileName = downloadFileName;
    }

    public String getDownloadFilePath() {
        return downloadFilePath == null ? "" : downloadFilePath;
    }

    public void setDownloadFilePath(String downloadFilePath) {
        this.downloadFilePath = downloadFilePath;
    }

    public long getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(long currentSize) {
        this.currentSize = currentSize;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public DownloadCallback getCallback() {
        return callback;
    }

    public void setCallback(DownloadCallback callback) {
        this.callback = callback;
    }
}
