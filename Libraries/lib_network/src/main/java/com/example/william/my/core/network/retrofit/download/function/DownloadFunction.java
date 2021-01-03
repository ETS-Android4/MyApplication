package com.example.william.my.core.network.retrofit.download.function;

import com.example.william.my.core.network.retrofit.download.database.table.DownloadTask;
import com.example.william.my.core.network.retrofit.download.state.DownloadState;

import io.reactivex.rxjava3.functions.Function;
import okhttp3.ResponseBody;

public class DownloadFunction implements Function<ResponseBody, DownloadTask> {

    private final DownloadTask downloadTask;

    public DownloadFunction(DownloadTask downloadTask) {
        this.downloadTask = downloadTask;
    }

    @Override
    public DownloadTask apply(ResponseBody body) throws Exception {

        downloadTask.setState(DownloadState.LOADING.getValue());//下载中

        downloadTask.setTotalSize(body.contentLength());

        return downloadTask;
    }
}

