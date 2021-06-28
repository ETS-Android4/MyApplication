package com.example.william.my.core.imageloader.glide;

import android.app.Activity;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.request.RequestOptions;
import com.example.william.my.core.imageloader.glide.module.GlideApp;
import com.example.william.my.core.imageloader.glide.transformation.RadiusTransformation;
import com.example.william.my.core.imageloader.glide.transformation.RadiusTransformation.CornerType;
import com.example.william.my.core.imageloader.loader.ILoader;

/**
 * https://github.com/bumptech/glide/
 * <p>
 * FitCenter：不剪裁，不填充ImageVIew (默认)
 * CenterCrop：剪裁，完全填充ImageVIew
 */
public class GlideLoader implements ILoader {

    @Override
    public void load(FragmentActivity activity, String url, ImageView imageView) {
        loadActivity(activity, url, imageView, new RequestOptions());
    }

    @Override
    public void load(FragmentActivity activity, String url, ImageView imageView, RequestOptions options) {
        loadActivity(activity, url, imageView, options);
    }

    @Override
    public void loadCircle(FragmentActivity activity, String url, ImageView imageView) {
        loadCircleActivity(activity, url, imageView);
    }

    @Override
    public void loadRadius(FragmentActivity activity, String url, ImageView imageView, int radius) {
        loadRadiusActivity(activity, url, imageView, radius, CornerType.ALL);
    }

    @Override
    public void loadRadius(FragmentActivity activity, String url, ImageView imageView, int radius, CornerType type) {
        loadRadiusActivity(activity, url, imageView, radius, type);
    }

    private void loadActivity(Activity activity, String url, ImageView imageView, RequestOptions options) {
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

    private void loadRadiusActivity(Activity activity, String url, ImageView imageView, int radius, CornerType type) {
        RequestOptions radiusOptions = new RequestOptions()
                .transform(new RadiusTransformation(radius, type));
        loadActivity(activity, url, imageView, radiusOptions);
    }
}
