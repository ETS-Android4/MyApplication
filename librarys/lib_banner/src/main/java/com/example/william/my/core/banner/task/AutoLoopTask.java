package com.example.william.my.core.banner.task;

import androidx.recyclerview.widget.RecyclerView;


import com.example.william.my.core.banner.Banner;
import com.example.william.my.core.banner.adapter.BannerAdapter;

import java.lang.ref.WeakReference;

public class AutoLoopTask<T, BA extends BannerAdapter<T, ? extends RecyclerView.ViewHolder>> implements Runnable {

    private final WeakReference<Banner<T, BA>> reference;

    public AutoLoopTask(Banner<T, BA> banner) {
        this.reference = new WeakReference<>(banner);
    }

    @Override
    public void run() {
        Banner<T, BA> banner = reference.get();
        if (banner != null && banner.isAutoLoop()) {
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
