package com.example.william.my.core.imageloader.loader;

import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;

import com.example.william.my.core.imageloader.corner.CornerType;

public interface ILoader {

    void load(FragmentActivity activity, String url, ImageView imageView);

    void loadCircle(FragmentActivity activity, String url, ImageView imageView);

    void loadRadius(FragmentActivity activity, String url, ImageView imageView, float radius);

    void loadRadius(FragmentActivity activity, String url, ImageView imageView, float radius, CornerType type);
}
