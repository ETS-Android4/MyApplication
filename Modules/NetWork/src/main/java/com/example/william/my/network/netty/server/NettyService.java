package com.example.william.my.network.netty.server;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class NettyService extends Service {

    private static final String TAG = "NettyService";

    private NettyServer nettyServer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    nettyServer = new NettyServer(NettyServer.DEFAULT_SERVER_PORT);
                    nettyServer.start();
                    Log.e(TAG, "Start NettyService Success...");
                } catch (Exception e) {
                    Log.e(TAG, "Start NettyService Failed...");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (nettyServer != null) {
            try {
                nettyServer.stop();
                Log.e(TAG, "Stop NettyService Success...");
            } catch (Exception e) {
                Log.e(TAG, "Stop NettyService Failed...");
                e.printStackTrace();
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    public static void startService(Context context) {
        Intent intent = new Intent(context, NettyService.class);
        context.startService(intent);
    }

    public static void stopService(Context context) {
        Intent intent = new Intent(context, NettyService.class);
        context.stopService(intent);
    }
}
