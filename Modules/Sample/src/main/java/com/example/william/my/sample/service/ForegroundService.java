package com.example.william.my.sample.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.william.my.sample.R;

/**
 * 前台服务
 */
public class ForegroundService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 首次创建服务时调用，如果服务已在运行，则不会调用此方法。该方法只被调用一次。
     * 在调用 onStartCommand() 或 onBind() 之前
     */
    @Override
    public void onCreate() {
        super.onCreate();

        // 非Activity场景启动Activity
        // Intent intent = new Intent(this, NotificationActivity.class);
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //不加 FLAG_ACTIVITY_NEW_TASK 将抛出异常
        // startActivity(intent);

        Notification notification = new NotificationCompat.Builder(this, "channelId")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("通知标题")//通知标题
                .setContentText("通知内容")//通知内容
                .build();
        startForeground(1000, notification);//将服务置于启动状态
    }

    /**
     * 每次通过startService()方法启动Service时都会被调用
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(ForegroundService.this, "前台服务已启动", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }
}
