package com.example.william.my.jet.adapter;

import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import com.example.william.my.jet.R;

public class BindingAdapters {

    @BindingAdapter(value = "setBackground")
    public static void setBackgroundColor(View view, String likes) {
        if (Integer.parseInt(likes) >= 3) {
            view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimary));
        }
    }
}
