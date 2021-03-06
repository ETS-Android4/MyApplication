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

import androidx.annotation.RequiresApi;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.demo.service.MyJobService;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.utils.T;

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
     * ?????? JobScheduler
     */
    public void scheduleJob() {
        /*
         * Builder??????????????????????????????
         * ??????????????????jobId?????????app?????????uid????????????Job,??????jobId??????????????????
         * ????????????????????????????????????JobService,?????????????????????????????????JobService??????onStartJob???onStopJob??????
         */
        JobInfo.Builder builder = new JobInfo.Builder(mJobId++, mServiceComponent);

        //????????????????????????????????????????????????.
        builder.setMinimumLatency(3 * 1000);

        //???????????????????????????????????????????????????
        builder.setOverrideDeadline(5 * 1000);

        //????????????????????????????????????????????????
        //JobInfo.NETWORK_TYPE_NONE????????????????????????????????????
        //JobInfo.NETWORK_TYPE_ANY???????????????????????????
        //JobInfo.NETWORK_TYPE_UNMETERED?????????????????????????????????
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);

        //????????????????????????
        builder.setRequiresDeviceIdle(true);
        //????????????????????????
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
     * ?????? JobScheduler
     */
    public void cancelAllJobs() {
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.cancelAll();
        T.show("All jobs cancelled");
    }

    /**
     * ?????? JobScheduler
     */
    public void finishJob() {
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        List<JobInfo> jobs = jobScheduler.getAllPendingJobs();
        if (jobs.size() > 0) {
            int jobId = jobs.get(0).getId();
            jobScheduler.cancel(jobId);
            T.show("?????? : ");
        } else {
            T.show("No jobs to cancel");
        }
    }
}
