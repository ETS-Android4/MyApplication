package com.example.william.my.module.jetpack.adapter;

import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import com.example.william.my.module.jetpack.R;

public class BindingAdapters {

    @BindingAdapter(value = "setBackground")
    public static void setBackgroundColor(View view, String likes) {
        if (Integer.parseInt(likes) >= 10) {
            view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimary));
        }
    }
}
