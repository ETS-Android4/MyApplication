package com.example.william.my.core.banner.indicator;

import android.view.View;

import androidx.annotation.NonNull;

import com.example.william.my.core.banner.config.BannerIndicatorConfig;
import com.example.william.my.core.banner.listener.BannerOnPageChangeListener;

public interface Indicator extends BannerOnPageChangeListener {
    @NonNull
    View getIndicatorView();

    BannerIndicatorConfig getIndicatorConfig();

    void onPageChanged(int count, int currentPosition);

}
