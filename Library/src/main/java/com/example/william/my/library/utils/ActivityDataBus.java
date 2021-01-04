package com.example.william.my.library.utils;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * ActivityDataBus
 */
public class ActivityDataBus {

    public static <T extends ViewModel> T getData(Context context, Class<T> tClass) {
        return getData(checkContext(context), tClass);
    }

    public static <T extends ViewModel> T getData(Activity context, Class<T> tClass) {
        return getData(checkContext(context), tClass);
    }

    public static <T extends ViewModel> T getData(AppCompatActivity context, Class<T> tClass) {
        //return ViewModelProviders.of(context).get(tClass);
        return new ViewModelProvider(context).get(tClass);
    }

    private static AppCompatActivity checkContext(Context context) {
        if (context instanceof AppCompatActivity) return (AppCompatActivity) context;
        throw new IllegalContextException();
    }

    private static class IllegalContextException extends RuntimeException {
        private IllegalContextException() {
            super("Can't create ViewModelProvider for detached fragment");
        }
    }

}
