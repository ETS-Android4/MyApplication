package com.example.william.my.network.nano;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;

public class HttpService extends Service {

    private static final String TAG = "HttpService";

    private HttpServer httpServer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            httpServer = new HttpServer();
            httpServer.start(30000);
            Log.e(TAG, "Start HttpService Success...");
        } catch (IOException e) {
            Log.e(TAG, "Start HttpService Failed...");
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (httpServer != null) {
            try {
                httpServer.stop();
                Log.e(TAG, "Stop HttpService Success...");
            } catch (Exception e) {
                Log.e(TAG, "Stop HttpService Failed...");
                e.printStackTrace();
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    public static void startService(Context context) {
        Intent intent = new Intent(context, HttpService.class);
        context.startService(intent);
    }

    public static void stopService(Context context) {
        Intent intent = new Intent(context, HttpService.class);
        context.stopService(intent);
    }
}
