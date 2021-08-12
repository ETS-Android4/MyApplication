package com.example.william.my.module.utils;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class Crash {

    private static final String TAG = Crash.class.getSimpleName();

    private static final Thread.UncaughtExceptionHandler DEFAULT_UNCAUGHT_EXCEPTION_HANDLER = Thread.getDefaultUncaughtExceptionHandler();

    private Crash() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void init() {
        init(null);
    }

    public static void init(final OnCrashListener onCrashListener) {
        new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(@NonNull final Thread t, @NonNull final Throwable e) {
                final String time = new SimpleDateFormat("yyyy_MM_dd-HH_mm_ss", Locale.CHINA).format(new Date());
                final String head = "************* Log Head ****************" +
                        "\nTime Of Crash      : " + time +
                        "\nDevice Manufacturer: " + Build.MANUFACTURER +
                        "\nDevice Model       : " + Build.MODEL +
                        "\nAndroid Version    : " + Build.VERSION.RELEASE +
                        "\nAndroid SDK        : " + Build.VERSION.SDK_INT +
                        "\nApp VersionName    : " + "" +
                        "\nApp VersionCode    : " + "" +
                        "\n************* Log Head ****************\n\n";
                final String crashInfo = head + e.getMessage();

                if (onCrashListener != null) {
                    onCrashListener.onCrash(crashInfo, e);
                } else {
                    Log.d(TAG, crashInfo);
                }

                if (DEFAULT_UNCAUGHT_EXCEPTION_HANDLER != null) {
                    DEFAULT_UNCAUGHT_EXCEPTION_HANDLER.uncaughtException(t, e);
                }
            }
        };
    }

    ///////////////////////////////////////////////////////////////////////////
    // interface
    ///////////////////////////////////////////////////////////////////////////

    public interface OnCrashListener {
        void onCrash(String crashInfo, Throwable e);
    }
}
