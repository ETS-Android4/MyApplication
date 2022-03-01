package com.example.william.my.module.network.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.william.my.bean.base.Urls;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.network.R;
import com.example.william.my.module.network.glide.GlideApp;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.NetWork.NetWork_Glide)
public class GlideActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basics_layout_image);

        ImageView mImageView = findViewById(R.id.basics_imageView);

        Glide.with(this)
                .load(Urls.Url_Image)
                .placeholder(R.drawable.ic_launcher)//占位图
                .error(R.drawable.ic_launcher)//加载失败
                .centerCrop()
                .override(200, 200)
                .skipMemoryCache(true)//内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)//磁盘缓存：NONE 什么都不缓存；SOURCE 只缓存全尺寸图；RESULT 只缓存最终的加载图；ALL 缓存所有版本图（默认）
                .priority(Priority.HIGH)//优先级
                .into(mImageView);
        GlideApp.with(this)
                .asBitmap()
                .load(Urls.Url_Image)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {

                    }

                    @Override
                    public void onLoadCleared(Drawable placeholder) {

                    }
                });
    }
}