/*
 * Copyright 2014 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.william.my.module.demo.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.william.my.module.demo.activity.JobActivity;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MyJobService extends JobService {

    private final String TAG = getClass().getSimpleName();

    private Messenger mActivityMessenger;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mActivityMessenger = intent.getParcelableExtra(JobActivity.KEY_MESSENGER);
        return START_NOT_STICKY;
    }

    @Override
    public boolean onStartJob(final JobParameters params) {
        sendMessage(JobActivity.MSG_COLOR_START, params.getJobId());

        long duration = params.getExtras().getLong(JobActivity.KEY_WORK_DURATION);

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendMessage(JobActivity.MSG_COLOR_STOP, params.getJobId());
                jobFinished(params, false);
            }
        }, duration);
        Log.e(TAG, "on start job: " + params.getJobId());

        // Return true as there's more work to be done with this job.
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        sendMessage(JobActivity.MSG_COLOR_STOP, params.getJobId());
        Log.e(TAG, "on stop job: " + params.getJobId());

        // Return false to drop the job.
        return false;
    }

    private void sendMessage(int messageID, Object params) {
        if (mActivityMessenger == null) {
            return;
        }
        Message message = Message.obtain();
        message.what = messageID;
        message.obj = params;
        try {
            mActivityMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
