package com.example.william.my.core.download.listener;

/**
 * 下载进度接口
 */
public interface DownloadListener {

    void onProgress(String url, int state, long currentSize, long totalLength);

}
