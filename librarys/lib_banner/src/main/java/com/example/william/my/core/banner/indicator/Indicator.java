package com.example.william.my.core.banner.indicator;

import android.view.View;

import androidx.annotation.NonNull;

import com.example.william.my.core.banner.listener.OnPageChangeListener;

public interface Indicator extends OnPageChangeListener {

    @NonNull
    View getIndicatorView();

    void onPageChanged(int count, int currentPosition);

}

