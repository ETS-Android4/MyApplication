package com.example.william.my.core.imageloader.glide;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.request.RequestOptions;
import com.example.william.my.core.imageloader.glide.target.GlideRadiusTransformation;

public class GlideLoader {

    private void loadActivity(Activity activity, String url, ImageView imageView,
                              RequestOptions options) {
        GlideApp.with(activity)
                .load(url)
                .fitCenter()
                .apply(options)
                .into(imageView);
    }

    private void loadCircleActivity(Activity activity, String url, ImageView imageView) {
        RequestOptions circleOptions = new RequestOptions().circleCrop();
        loadActivity(activity, url, imageView, circleOptions);
    }

    private void loadRadiusActivity(Activity activity, String url, ImageView imageView, int radius) {
        RequestOptions radiusOptions = new RequestOptions()
                .transform(new GlideRadiusTransformation(radius));
        loadActivity(activity, url, imageView, radiusOptions);
    }
}
