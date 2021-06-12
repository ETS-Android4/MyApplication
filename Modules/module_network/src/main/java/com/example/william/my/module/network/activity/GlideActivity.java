package com.example.william.my.module.network.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.network.R;
import com.example.william.my.module.network.glide.GlideApp;
import com.example.william.my.module.network.glide.MyAppGlideModule;
import com.example.william.my.module.router.ARouterPath;

/**
 * https://github.com/bumptech/glide/
 * {@link MyAppGlideModule}
 * CenterCrop：剪裁，完全填充ImageVIew
 * FitCenter：不剪裁，不填充ImageVIew
 */
@Route(path = ARouterPath.NetWork.NetWork_Glide)
public class GlideActivity extends BaseActivity {

    private boolean b;
    private ImageView mImageView;
    private static final String URL_IMG = "https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basics_layout_image);

        mImageView = findViewById(R.id.basics_imageView);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b = !b;
                if (b) {
                    loadImage();
                } else {
                    loadRoundImage();
                }
            }
        });
    }

    /**
     * 基本用法
     */
    private void loadImage() {
        GlideApp
                .with(this)
                .load(URL_IMG)
                .placeholder(R.drawable.ic_launcher)
                //.skipMemoryCache(true)//跳过内存缓存，默认为false
                //.diskCacheStrategy(DiskCacheStrategy.RESOURCE)//设置磁盘缓存。NONE：不缓存；DATA：解码前；RESOURCE：解码后
                .into(mImageView);
    }

    /**
     * 圆形图片
     */
    private void loadRoundImage() {
        GlideApp.with(this)
                .asBitmap()//.asBitmap()在.load()之前调用
                .load(URL_IMG)
                .centerCrop()
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                        drawable.setCircular(true);
                        mImageView.setImageDrawable(drawable);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    /**
     * 手动缓存
     */
    private void loadImageByCache() {
        GlideApp
                .with(this)
                .asBitmap()//在.load()之前调用
                .load(URL_IMG)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        /*
                         * 内存缓存
                         */
                        //CacheMemoryStaticUtils.put(url, resource);
                        //mImageView.setImageBitmap((Bitmap) CacheMemoryStaticUtils.get(url));
                        /*
                         * 磁盘缓存
                         */
                        //CacheDiskStaticUtils.put(url, resource);
                        //mImageView.setImageBitmap((Bitmap) CacheDiskStaticUtils.getBitmap(url));
                        /*
                         * 二级缓存
                         */
                        //CacheDoubleStaticUtils.put(url, resource);
                        //mImageView.setImageBitmap((Bitmap) CacheDoubleStaticUtils.getBitmap(url));
                        mImageView.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //磁盘缓存清理（子线程）
        //GlideApp.get(this).clearDiskCache();
        //内存缓存清理（主线程）
        //GlideApp.get(this).clearMemory();
    }
}