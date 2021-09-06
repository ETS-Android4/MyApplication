package com.example.william.my.core.banner.indic;

import android.view.View;

import androidx.annotation.NonNull;

import com.example.william.my.core.banner.listener.BannerOnPageChangeListener;

public interface Indicator extends BannerOnPageChangeListener {

    @NonNull
    View getIndicatorView();

    void onPageChanged(int count, int currentPosition);

}

