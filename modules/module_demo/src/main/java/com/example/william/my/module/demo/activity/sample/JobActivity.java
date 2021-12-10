package com.example.william.my.module.demo.activity.sample;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.demo.service.MyJobService;
import com.example.william.my.module.router.ARouterPath;

import java.lang.ref.WeakReference;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
@Route(path = ARouterPath.Demo.Demo_JobScheduler)
public class JobActivity extends BaseResponseActivity {

    public static final int MSG_COLOR_START = 0;
    public static final int MSG_COLOR_STOP = 1;

    public static final String KEY_MESSENGER = ".MESSENGER_INTENT_KEY";
    public static final String KEY_WORK_DURATION = ".WORK_DURATION_KEY";

    private ComponentName mServiceComponent;

    private int mJobId = 0;

    private static class JobHandler extends Handler {

        private final WeakReference<JobActivity> weakReference;


        private JobHandler(JobActivity activity) {
            super(Looper.getMainLooper());
            this.weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            JobActivity mActivity = weakReference.get();
            if (mActivity == null) {
                return;
            }
            switch (msg.what) {
                case MSG_COLOR_START:
                    // Start received, turn on the indicator and show text.
                    mActivity.showResponse(String.format("Job ID %s %s", msg.obj, "started"));
                    break;
                case MSG_COLOR_STOP:
                    // Stop received, turn on the indicator and show text.
                    mActivity.showResponse(String.format("Job ID %s %s", msg.obj, "stopped"));
                    break;
            }
        }
    }

    @Override
    public void initView() {
        super.initView();
        mServiceComponent = new ComponentName(this, MyJobService.class);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        scheduleJob();
    }

    @Override
    protected void onStart() {
        super.onStart();
        JobHandler jobHandler = new JobHandler(this);
        Messenger jobMessenger = new Messenger(jobHandler);
        Intent intent = new Intent(this, MyJobService.class);
        intent.putExtra(KEY_MESSENGER, jobMessenger);
        startService(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(new Intent(this, MyJobService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelAllJobs();
    }

    /**
     * 执行 JobScheduler
     */
    public void scheduleJob() {
        /*
         * Builder构造方法接收两个参数
         * 第一个参数是jobId，每个app或者说uid下不同的Job,它的jobId必须是不同的
         * 第二个参数是我们自定义的JobService,系统会回调我们自定义的JobService中的onStartJob和onStopJob方法
         */
        JobInfo.Builder builder = new JobInfo.Builder(mJobId++, mServiceComponent);

        //设置至少延迟多久后执行，单位毫秒.
        builder.setMinimumLatency(3 * 1000);

        //设置最多延迟多久后执行，单位毫秒。
        builder.setOverrideDeadline(5 * 1000);

        //设置需要的网络条件，有三个取值：
        //JobInfo.NETWORK_TYPE_NONE（无网络时执行，默认）、
        //JobInfo.NETWORK_TYPE_ANY（有网络时执行）、
        //JobInfo.NETWORK_TYPE_UNMETERED（网络无需付费时执行）
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);

        //是否在空闲时执行
        builder.setRequiresDeviceIdle(true);
        //是否在充电时执行
        builder.setRequiresCharging(true);

        // Extras, work duration.
        PersistableBundle extras = new PersistableBundle();
        extras.putLong(KEY_WORK_DURATION, 1000);
        builder.setExtras(extras);

        // Schedule job
        showResponse("Scheduling job");
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
    }

    /**
     * 取消 JobScheduler
     */
    public void cancelAllJobs() {
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.cancelAll();
        Toast.makeText(JobActivity.this, "All jobs cancelled", Toast.LENGTH_SHORT).show();
    }

    /**
     * 完成 JobScheduler
     */
    public void finishJob() {
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        List<JobInfo> jobs = jobScheduler.getAllPendingJobs();
        if (jobs.size() > 0) {
            int jobId = jobs.get(0).getId();
            jobScheduler.cancel(jobId);
            Toast.makeText(JobActivity.this, "取消 : " + jobId, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(JobActivity.this, "No jobs to cancel", Toast.LENGTH_SHORT).show();
        }
    }
}
