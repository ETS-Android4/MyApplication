package com.example.william.my.module.network.nano;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.william.my.module.utils.L;

import java.io.IOException;

public class NanoService extends Service {

    private static final String TAG = "NanoService";

    private NanoServer nanoServer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            nanoServer = new NanoServer();
            nanoServer.start(30000);
            L.i(TAG, "Start HttpService Success...");
        } catch (IOException e) {
            L.i(TAG, "Start HttpService Failed...");
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (nanoServer != null) {
            try {
                nanoServer.stop();
                L.i(TAG, "Stop HttpService Success...");
            } catch (Exception e) {
                L.i(TAG, "Stop HttpService Failed...");
                e.printStackTrace();
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    public static void startService(Context context) {
        Intent intent = new Intent(context, NanoService.class);
        context.startService(intent);
    }

    public static void stopService(Context context) {
        Intent intent = new Intent(context, NanoService.class);
        context.stopService(intent);
    }
}
