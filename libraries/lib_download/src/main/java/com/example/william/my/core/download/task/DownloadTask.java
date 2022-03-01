package com.example.william.my.core.download.task;

import androidx.annotation.NonNull;

import com.example.william.my.core.download.callback.DownloadCallback;
import com.example.william.my.core.download.state.DownloadState;

import java.io.Serializable;

public class DownloadTask implements Serializable {

    private String downloadUrl;

    private String downloadFileName;

    private String downloadFilePath;

    /**
     * 当前大小
     */
    private long currentSize;

    /**
     * 文件大小
     */
    private long contentLength;

    private int state = DownloadState.NONE.getValue();

    private DownloadCallback mDownloadCallback;

    public DownloadTask(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public DownloadTask(String url, String name) {
        setDownloadUrl(url);
        setDownloadFileName(name);
    }

    public DownloadTask(String url, String name, DownloadCallback callback) {
        setDownloadUrl(url);
        setDownloadFileName(name);

        setDownloadCallback(callback);
    }


    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
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

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public DownloadCallback getDownloadCallback() {
        return mDownloadCallback;
    }

    public void setDownloadCallback(DownloadCallback callback) {
        this.mDownloadCallback = callback;
    }
}
