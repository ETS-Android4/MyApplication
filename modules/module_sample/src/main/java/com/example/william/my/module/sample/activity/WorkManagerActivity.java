package com.example.william.my.module.sample.activity;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.sample.worker.UploadWorker;
import com.example.william.my.module.router.ARouterPath;

import java.util.concurrent.TimeUnit;

/**
 * https://developer.android.google.cn/topic/libraries/architecture/workmanager
 * 用于可延迟运行并且在应用退出或设备重启时必须能够可靠运行的任务。例如：
 * 向后端服务发送日志或分析数据，定期将应用数据与服务器同步
 */
@Route(path = ARouterPath.Sample.Sample_WorkManager)
public class WorkManagerActivity extends BaseResponseActivity {

    private OneTimeWorkRequest oneTimeWorkRequest;

    private PeriodicWorkRequest periodicWorkRequest;

    @Override
    public void initView() {
        super.initView();
        initWork();
    }

    private void initWork() {
        // 工作约束
        Constraints constraints = new Constraints.Builder()
                //网络状态
                .setRequiredNetworkType(NetworkType.CONNECTED)
                //非低电量
                //.setRequiresBatteryNotLow(true)
                //设备待机空闲
                //.setRequiresDeviceIdle(true)
                //内存不紧张
                //.setRequiresStorageNotLow(true)
                //充电状态
                //.setRequiresCharging(true)
                .build();

        // 定义任务的输入/输出
        Data imageData = new Data.Builder()
                .putString("key", "inputData")
                .build();

        // 配置运行任务的方式和时间。
        // OneTimeWorkRequest 只执行一次的任务请求，支持任务链
        oneTimeWorkRequest = new OneTimeWorkRequest.Builder(UploadWorker.class)
                //初始延迟
                .setInitialDelay(3, TimeUnit.SECONDS)
                //工作约束
                .setConstraints(constraints)
                //传递数据
                .setInputData(imageData)
                //标记工作
                .addTag("upload")
                .build();

        // 配置运行任务的方式和时间。
        // PeriodicWorkRequest 可以多次、定时执行的任务请求，不支持任务链。可以定义的最短重复间隔是 15 分钟
        periodicWorkRequest = new PeriodicWorkRequest.Builder(UploadWorker.class, 15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();

        //任务提交给系统
        WorkManager.getInstance(this)
                .enqueue(oneTimeWorkRequest);

        //链式执行
        //WorkManager.getInstance(this)
        //         //并列运行
        //        .beginWith(Arrays.asList(uploadWorkRequest, uploadWorkRequest, uploadWorkRequest))
        //         //链式运行
        //        .then(uploadWorkRequest)
        //        .enqueue();

        // 唯一工作
        //WorkManager.getInstance(this)
        //        .beginUniqueWork("name", ExistingWorkPolicy.REPLACE, uploadWorkRequest)
        //        .enqueue();

        //观察工作
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(oneTimeWorkRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {
                        if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                            // 工作进度
                            Data progress = workInfo.getProgress();
                            Log.d(TAG, "Work finished!");
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消任务
        WorkManager.getInstance(this).cancelWorkById(oneTimeWorkRequest.getId());
        WorkManager.getInstance(this).cancelWorkById(periodicWorkRequest.getId());
    }
}