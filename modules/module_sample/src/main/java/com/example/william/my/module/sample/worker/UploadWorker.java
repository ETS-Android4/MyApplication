package com.example.william.my.module.sample.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

/**
 * Worker,CoroutineWorker,RxWorker
 */
public class UploadWorker extends Worker {

    private static final String TAG = "UploadWorker";

    public UploadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Get the input
        String input = getInputData().getString("key");

        // Do the work here--in this case, upload the images.
        uploadImages(input);

        // Create the output of the work
        // Data 对象的大小上限为 10KB。
        Data outputData = new Data.Builder()
                .putString("key", "outData")
                .build();

        //更新进度
        setProgressAsync(outputData);

        // Indicate whether the task finished successfully with the Result
        return Result.success(outputData);
    }

    private void uploadImages(String s) {
        Log.e(TAG, "图片上传: " + s);
    }
}
