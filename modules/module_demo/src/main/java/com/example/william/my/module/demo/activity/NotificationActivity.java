package com.example.william.my.module.demo.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.demo.R;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.Demo.Demo_Notification)
public class NotificationActivity extends BaseResponseActivity {

    private NotificationManager mNotificationManager;

    @Override
    public void initView() {
        super.initView();

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    "channelId",
                    "channelName",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setShowBadge(true);//显示角标
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public void setOnClick() {
        super.setOnClick();
        //Intent intent = new Intent(NotificationActivity.this, ListViewActivity.class);
        //PendingIntent pendIntent = PendingIntent.getActivity(NotificationActivity.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Notification notification = new NotificationCompat.Builder(NotificationActivity.this, "channelId")
                //.setContentIntent(pendIntent)
                //.setPriority(NotificationManager.IMPORTANCE_HIGH)//设置优先级//@RequiresApi(api = Build.VERSION_CODES.N)
                //.setWhen(System.currentTimeMillis())//设置通知时间，默认为系统发出通知的时间，通常不用设置
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("通知标题")//通知标题
                .setContentText("通知内容")//通知内容
                .setAutoCancel(true)
                .build();
        mNotificationManager.notify(1, notification);//发送通知,id=1
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager.deleteNotificationChannel("channelId");
        }
    }
}
