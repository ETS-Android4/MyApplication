package com.example.william.my.module.sample.activity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.sample.R;
import com.example.william.my.module.sample.service.MyJobService;

import java.lang.ref.WeakReference;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
@Route(path = ARouterPath.Sample.Sample_JobScheduler)
public class JobSchedulerActivity extends AppCompatActivity {

    private static final String TAG = JobSchedulerActivity.class.getSimpleName();

    public static final int MSG_UNCOLOR_START = 0;
    public static final int MSG_UNCOLOR_STOP = 1;
    public static final int MSG_COLOR_START = 2;
    public static final int MSG_COLOR_STOP = 3;

    public static final String MESSENGER_INTENT_KEY = ".MESSENGER_INTENT_KEY";
    public static final String WORK_DURATION_KEY = ".WORK_DURATION_KEY";

    private EditText mDelayEditText;
    private EditText mDeadlineEditText;
    private EditText mDurationTimeEditText;
    private RadioButton mWiFiConnectivityRadioButton;
    private RadioButton mAnyConnectivityRadioButton;
    private CheckBox mRequiresChargingCheckBox;
    private CheckBox mRequiresIdleCheckbox;

    private ComponentName mServiceComponent;

    private int mJobId = 0;

    // Handler for incoming messages from the service.
    private JobSchedulerMessageHandler mHandler;

    /**
     * A {@link Handler} allows you to send messages associated with a thread. A {@link Messenger}
     * uses this handler to communicate from {@link MyJobService}. It's also used to make
     * the start and stop views blink for a short period of time.
     */
    private static class JobSchedulerMessageHandler extends Handler {

        // Prevent possible leaks with a weak reference.
        private WeakReference<JobSchedulerActivity> mActivity;

        JobSchedulerMessageHandler(JobSchedulerActivity activity) {
            super(/* default looper */);
            this.mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            JobSchedulerActivity mainActivity = mActivity.get();
            if (mainActivity == null) {
                return;
            }
            View showStartView = mainActivity.findViewById(R.id.onstart_textview);
            View showStopView = mainActivity.findViewById(R.id.onstop_textview);
            Message m;
            switch (msg.what) {
                /*
                 * Receives callback from the service when a job has landed
                 * on the app. Turns on indicator and sends a message to turn it off after
                 * a second.
                 */
                case MSG_COLOR_START:
                    // Start received, turn on the indicator and show text.
                    showStartView.setBackgroundColor(getColor(R.color.colorAccent));
                    updateParamsTextView(msg.obj, "started");

                    // Send message to turn it off after a second.
                    m = Message.obtain(this, MSG_UNCOLOR_START);
                    sendMessageDelayed(m, 1000L);
                    break;
                /*
                 * Receives callback from the service when a job that previously landed on the
                 * app must stop executing. Turns on indicator and sends a message to turn it
                 * off after two seconds.
                 */
                case MSG_COLOR_STOP:
                    // Stop received, turn on the indicator and show text.
                    showStopView.setBackgroundColor(getColor(R.color.colorPrimaryDark));
                    updateParamsTextView(msg.obj, "stopped");

                    // Send message to turn it off after a second.
                    m = obtainMessage(MSG_UNCOLOR_STOP);
                    sendMessageDelayed(m, 2000L);
                    break;
                case MSG_UNCOLOR_START:
                    showStartView.setBackgroundColor(getColor(R.color.colorPrimary));
                    updateParamsTextView(null, "");
                    break;
                case MSG_UNCOLOR_STOP:
                    showStopView.setBackgroundColor(getColor(R.color.colorPrimary));
                    updateParamsTextView(null, "");
                    break;
            }
        }

        private void updateParamsTextView(Object jobId, String action) {
            TextView paramsTextView = (TextView) mActivity.get().findViewById(R.id.task_params);
            if (jobId == null) {
                paramsTextView.setText("");
                return;
            }
            String jobIdText = String.valueOf(jobId);
            paramsTextView.setText(String.format("Job ID %s %s", jobIdText, action));
        }

        private int getColor(int color) {
            return mActivity.get().getResources().getColor(color);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_main);

        // Set up UI.
        mDelayEditText = (EditText) findViewById(R.id.delay_time);
        mDurationTimeEditText = (EditText) findViewById(R.id.duration_time);
        mDeadlineEditText = (EditText) findViewById(R.id.deadline_time);
        mWiFiConnectivityRadioButton = (RadioButton) findViewById(R.id.checkbox_unmetered);
        mAnyConnectivityRadioButton = (RadioButton) findViewById(R.id.checkbox_any);
        mRequiresChargingCheckBox = (CheckBox) findViewById(R.id.checkbox_charging);
        mRequiresIdleCheckbox = (CheckBox) findViewById(R.id.checkbox_idle);

        mServiceComponent = new ComponentName(this, MyJobService.class);

        mHandler = new JobSchedulerMessageHandler(this);
    }

    @Override
    protected void onStop() {
        stopService(new Intent(this, MyJobService.class));
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, MyJobService.class);
        intent.putExtra(MESSENGER_INTENT_KEY, new Messenger(mHandler));
        startService(intent);
    }

    /**
     * 执行 JobScheduler
     */
    public void scheduleJob(View v) {
        /*
         * Builder构造方法接收两个参数
         * 第一个参数是jobId，每个app或者说uid下不同的Job,它的jobId必须是不同的
         * 第二个参数是我们自定义的JobService,系统会回调我们自定义的JobService中的onStartJob和onStopJob方法
         */
        JobInfo.Builder builder = new JobInfo.Builder(mJobId++, mServiceComponent);

        String delay = mDelayEditText.getText().toString();
        if (!TextUtils.isEmpty(delay)) {
            builder.setMinimumLatency(Long.valueOf(delay) * 1000);
        }
        String deadline = mDeadlineEditText.getText().toString();
        if (!TextUtils.isEmpty(deadline)) {
            builder.setOverrideDeadline(Long.valueOf(deadline) * 1000);
        }
        boolean requiresUnmetered = mWiFiConnectivityRadioButton.isChecked();
        boolean requiresAnyConnectivity = mAnyConnectivityRadioButton.isChecked();
        if (requiresUnmetered) {
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
        } else if (requiresAnyConnectivity) {
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        }
        builder.setRequiresDeviceIdle(mRequiresIdleCheckbox.isChecked());
        builder.setRequiresCharging(mRequiresChargingCheckBox.isChecked());

        // Extras, work duration.
        PersistableBundle extras = new PersistableBundle();
        String workDuration = mDurationTimeEditText.getText().toString();
        if (TextUtils.isEmpty(workDuration)) {
            workDuration = "1";
        }
        extras.putLong(WORK_DURATION_KEY, Long.valueOf(workDuration) * 1000);

        builder.setExtras(extras);

        // Schedule job
        Log.e(TAG, "Scheduling job");
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
    }

    /**
     * 取消 JobScheduler
     */
    public void cancelAllJobs(View v) {
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.cancelAll();
        Toast.makeText(JobSchedulerActivity.this, "All jobs cancelled", Toast.LENGTH_SHORT).show();
    }

    /**
     * 完成 JobScheduler
     */
    public void finishJob(View v) {
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        List<JobInfo> jobs = jobScheduler.getAllPendingJobs();
        if (jobs.size() > 0) {
            int jobId = jobs.get(0).getId();
            jobScheduler.cancel(jobId);
            Toast.makeText(JobSchedulerActivity.this, "取消 : " + jobId, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(JobSchedulerActivity.this, "No jobs to cancel", Toast.LENGTH_SHORT).show();
        }
    }
}
