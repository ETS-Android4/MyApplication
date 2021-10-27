package com.example.william.my.core.download.callback;

import com.example.william.my.core.download.task.DownloadTask;

public interface DownloadCallback {

    /**
     * 下载进度
     *
     * @param downloadTask
     */
    void onProgress(DownloadTask downloadTask);
}
