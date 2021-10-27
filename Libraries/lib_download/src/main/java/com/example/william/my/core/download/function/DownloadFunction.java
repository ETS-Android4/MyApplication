package com.example.william.my.core.download.function;

import com.example.william.my.core.download.task.DownloadTask;

import io.reactivex.rxjava3.functions.Function;
import okhttp3.ResponseBody;

public class DownloadFunction implements Function<ResponseBody, DownloadTask> {

    private final DownloadTask downloadTask;

    public DownloadFunction(DownloadTask downloadTask) {
        this.downloadTask = downloadTask;
    }

    @Override
    public DownloadTask apply(ResponseBody body) throws Exception {

        downloadTask.setContentLength(body.contentLength());

        return downloadTask;
    }
}

