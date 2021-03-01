package com.example.william.my.module.jetpack.activity;

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
import com.example.william.my.module.jetpack.worker.UploadWorker;
import com.example.william.my.module.router.ARouterPath;

import java.util.concurrent.TimeUnit;

/**
 * https://developer.android.google.cn/topic/libraries/architecture/workmanager
 * 用于可延迟运行并且在应用退出或设备重启时必须能够可靠运行的任务。例如：
 * 向后端服务发送日志或分析数据，定期将应用数据与服务器同步
 */
@Route(path = ARouterPath.JetPack.JetPack_WorkManager)
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
                .setRequiredNetworkType(NetworkType.CONNECTED) //网络状态
                //.setRequiresBatteryNotLow(true) //非低电量
                //.setRequiresDeviceIdle(true)//设备待机空闲
                //.setRequiresStorageNotLow(true)//内存不紧张
                //.setRequiresCharging(true) //充电状态
                .build();

        // 定义任务的输入/输出
        Data imageData = new Data.Builder()
                .putString("key", "inputData")
                .build();

        // 配置运行任务的方式和时间。
        // OneTimeWorkRequest 只执行一次的任务请求，支持任务链
        oneTimeWorkRequest = new OneTimeWorkRequest.Builder(UploadWorker.class)
                .setInitialDelay(3, TimeUnit.SECONDS)//初始延迟
                .setConstraints(constraints)//工作约束
                .setInputData(imageData)//传递数据
                .addTag("upload")//标记工作
                .build();

        // PeriodicWorkRequest 可以多次、定时执行的任务请求，不支持任务链。可以定义的最短重复间隔是 15 分钟
        periodicWorkRequest = new PeriodicWorkRequest.Builder(UploadWorker.class, 15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();

        //任务提交给系统
        WorkManager.getInstance(this)
                .enqueue(oneTimeWorkRequest);

        //链式执行
        //WorkManager.getInstance(this)
        //        .beginWith(Arrays.asList(uploadWorkRequest, uploadWorkRequest, uploadWorkRequest))//并列运行
        //        .then(uploadWorkRequest)//链式运行
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
                            Log.e(TAG, "Work finished!");
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