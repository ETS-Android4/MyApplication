package com.example.william.my.core.banner.observer;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

public interface BannerLifecycleObserver extends LifecycleObserver {

    void onResume(LifecycleOwner owner);

    void onPause(LifecycleOwner owner);

    void onDestroy(LifecycleOwner owner);
}
