package com.example.william.my.core.banner.indicator;

import android.view.View;

import androidx.annotation.NonNull;

import com.example.william.my.core.banner.listener.BannerOnPageChangeListener;

public interface Indicator extends BannerOnPageChangeListener {

    @NonNull
    View getIndicatorView();

    void onPageChanged(int count, int currentPosition);

}

