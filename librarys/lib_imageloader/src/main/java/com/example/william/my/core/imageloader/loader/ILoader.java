package com.example.william.my.core.imageloader.loader;

import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.request.RequestOptions;
import com.example.william.my.core.imageloader.glide.transformation.RadiusTransformation.CornerType;

public interface ILoader {

    void load(FragmentActivity activity, String url, ImageView imageView);

    void load(FragmentActivity activity, String url, ImageView imageView, RequestOptions options);

    void loadCircle(FragmentActivity activity, String url, ImageView imageView);

    void loadRadius(FragmentActivity activity, String url, ImageView imageView, int radius);

    void loadRadius(FragmentActivity activity, String url, ImageView imageView, int radius, CornerType type);
}
