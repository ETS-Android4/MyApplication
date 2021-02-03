package com.example.william.my.module.network.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.william.my.module.network.R;
import com.example.william.my.module.network.base.GlideApp;
import com.example.william.my.module.network.base.MyAppGlideModule;
import com.example.william.my.module.router.ARouterPath;

/**
 * https://github.com/bumptech/glide/
 * {@link MyAppGlideModule}
 */
@Route(path = ARouterPath.NetWork.NetWork_Glide)
public class GlideActivity extends AppCompatActivity {

    private ImageView mImageView;
    private static final String url = "https://www.baidu.com/img/baidu_jgylogo1.gif";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basics_layout_image);

        mImageView = findViewById(R.id.basics_imageView);

        //基本用法
        //GlideApp
        //        .with(this)
        //        //.asBitmap()//.asBitmap()在.load()之前调用
        //        .load(url)
        //        .placeholder(R.mipmap.basics_ic_launcher)
        //        .skipMemoryCache(true)//跳过内存缓存
        //        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)//设置磁盘缓存。NONE：不缓存；DATA：解码前；RESOURCE：解码后
        //        .into(mImageView);

        //圆形图片
        //GlideApp.with(this)
        //        .asBitmap()//.asBitmap()在.load()之前调用
        //        .load(url)
        //        .centerCrop()
        //        .into(new BitmapImageViewTarget(mImageView) {
        //            @Override
        //            protected void setResource(Bitmap resource) {
        //                RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
        //                drawable.setCircular(true);
        //                mImageView.setImageDrawable(drawable);
        //            }
        //        });

        GlideApp
                .with(this)
                .asBitmap()//在.load()之前调用
                .load(url)
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
}