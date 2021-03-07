package com.example.william.my.module.bugly.app;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.multidex.MultiDex;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.interfaces.BetaPatchListener;
import com.tencent.tinker.entry.DefaultApplicationLike;

import java.util.Locale;

public class SampleApplicationLike extends DefaultApplicationLike {

    private Context mContext;

    public static final String TAG = "Tinker";

    public SampleApplicationLike(Application application, int tinkerFlags,
                                 boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime,
                                 long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
        // 调试时，将第三个参数改为true
        Bugly.init(getApplication(), "f3c3164a42", false);

        mContext = getApplication();
        //补丁回调接口
        Beta.betaPatchListener = new BetaPatchListener() {
            @Override
            public void onPatchReceived(String s) {
                Log.e(TAG, "补丁下载地址：" + s);
            }

            @Override
            public void onDownloadReceived(long l, long l1) {
                Log.e(TAG, String.format(Locale.getDefault(), "%s %d%%",
                        Beta.strNotificationDownloading,
                        (int) (l1 == 0 ? 0 : l * 100 / l1)));
            }

            @Override
            public void onDownloadSuccess(String s) {
                Log.e(TAG, "补丁下载成功");
                Toast.makeText(mContext, "补丁下载成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownloadFailure(String s) {
                Log.e(TAG, "补丁下载失败");
                Toast.makeText(mContext, "补丁下载失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onApplySuccess(String s) {
                Log.e(TAG, "补丁应用成功");
                Toast.makeText(mContext, "补丁应用成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onApplyFailure(String s) {
                Log.e(TAG, "补丁应用失败");
                Toast.makeText(mContext, "补丁应用失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPatchRollback() {

            }
        };
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        // 安装tinker
        // TinkerManager.installTinker(this); 替换成下面Bugly提供的方法
        Beta.installTinker(this);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallback(Application.ActivityLifecycleCallbacks callbacks) {
        getApplication().registerActivityLifecycleCallbacks(callbacks);
    }

}