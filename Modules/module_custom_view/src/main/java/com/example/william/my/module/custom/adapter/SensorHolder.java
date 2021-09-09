package com.example.william.my.module.custom.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.william.my.core.banner.sensor.SensorLayout;
import com.example.william.my.module.custom.R;

public class SensorHolder extends RecyclerView.ViewHolder {

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
