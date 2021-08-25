package com.example.william.my.core.banner.task;

import com.example.william.my.core.banner.Banner;

import java.lang.ref.WeakReference;

public class AutoLoopTask implements Runnable{

    private final WeakReference<Banner> reference;

    public AutoLoopTask(Banner banner) {
        this.reference = new WeakReference<>(banner);
    }

    @Override
    public void run() {
        Banner banner = reference.get();
        if (banner != null && banner.mIsAutoLoop) {
            int count = banner.getItemCount();
            if (count == 0) {
                return;
            }
            int next = (banner.getCurrentItem() + 1) % count;
            banner.setCurrentItem(next);
            banner.postDelayed(banner.mLoopTask, banner.mLoopTime);
        }
    }
}
