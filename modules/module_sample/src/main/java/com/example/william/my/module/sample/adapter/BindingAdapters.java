package com.example.william.my.module.sample.adapter;

import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import com.example.william.my.module.sample.R;

/**
 * 自定义BindingAdapter
 */
public class BindingAdapters {

    @BindingAdapter(value = "setColor")
    public static void setBackgroundColor(View view, String likes) {
        if (Integer.parseInt(likes) >= 3) {
            view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimary));
        }
    }
}
