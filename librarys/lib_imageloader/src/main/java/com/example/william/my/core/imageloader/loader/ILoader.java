package com.example.william.my.core.imageloader.loader;

import android.app.Activity;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.request.RequestOptions;

public interface ILoader {

    void load(Activity activity, String url, ImageView imageView);

    void load(Activity activity, String url, ImageView imageView, RequestOptions options);

    void load(FragmentActivity activity, String url, ImageView imageView);

    void load(FragmentActivity activity, String url, ImageView imageView, RequestOptions options);

    void load(Fragment fragment, String url, ImageView imageView);

    void load(Fragment fragment, String url, ImageView imageView, RequestOptions options);
}
