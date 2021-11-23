package com.example.my.module.libraries.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my.module.libraries.R;
import com.example.william.my.core.banner.adapter.BannerAdapter;
import com.example.william.my.core.banner.sensor.SensorLayout;

import java.util.List;

public class SensorAdapter extends BannerAdapter<String, SensorAdapter.SensorHolder> {

    public SensorAdapter(List<String> mData) {
        super(mData);
    }

    @Override
    public SensorHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new SensorHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lib_item_sensor, parent, false));
    }

    @Override
    public void onBindView(SensorHolder holder, String data, int position, int size) {
        holder.mBackground.setScaleX(1.3f);
        holder.mBackground.setScaleY(1.2f);
        holder.mBackground.setDirection(-2);
        holder.mMid.setDirection(1);
        holder.mForeground.setDirection(4);
    }

    public static class SensorHolder extends RecyclerView.ViewHolder {

        public SensorLayout mForeground;//最上层传感器View
        public SensorLayout mMid;//中间层传感器View
        public SensorLayout mBackground;//最底层传感器View

        public SensorHolder(@NonNull View view) {
            super(view);
            mForeground = view.findViewById(R.id.foreground);
            mMid = view.findViewById(R.id.mid);
            mBackground = view.findViewById(R.id.background);
        }

    }
}
