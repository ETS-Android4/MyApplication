package com.example.william.my.module.custom.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.william.my.core.banner.adapter.BannerAdapter;
import com.example.william.my.module.custom.R;

import java.util.List;

public class SensorAdapter extends BannerAdapter<String, SensorHolder> {

    public SensorAdapter(List<String> mData) {
        super(mData);
    }

    @Override
    public SensorHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new SensorHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_sensor, parent, false));
    }

    @Override
    public void onBindView(SensorHolder holder, String data, int position, int size) {
        holder.mBackground.setScaleX(1.3f);
        holder.mBackground.setScaleY(1.2f);
        holder.mBackground.setDirection(-2);
        holder.mMid.setDirection(1);
        holder.mForeground.setDirection(4);
    }
}
