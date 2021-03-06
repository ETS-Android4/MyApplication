package com.example.william.my.module.network.socket;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.example.william.my.module.utils.L;

public class SocketService extends Service {

    private static final String TAG = "SocketService";

    private SocketServer socketServer;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            socketServer = new SocketServer(SocketServer.DEFAULT_SERVER_PORT);
            socketServer.setReuseAddr(true);
            socketServer.start();
            L.i(TAG, "Start ServerSocket Success...");
        } catch (Exception e) {
            L.i(TAG, "Start ServerSocket Failed...");
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (socketServer != null) {
            try {
                socketServer.stop();
                L.i(TAG, "Stop ServerSocket Success...");
            } catch (Exception e) {
                L.i(TAG, "Stop ServerSocket Failed...");
                e.printStackTrace();
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    public static void startService(Context context) {
        Intent intent = new Intent(context, SocketService.class);
        context.startService(intent);
    }

    public static void stopService(Context context) {
        Intent intent = new Intent(context, SocketService.class);
        context.stopService(intent);
    }
}
