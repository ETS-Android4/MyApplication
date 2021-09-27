package com.example.william.my.module.open.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import com.example.william.my.core.ninepatch.NinePatchChunk;
import com.example.william.my.core.ninepatch.builder.NinePatchBuilder;
import com.example.william.my.module.open.R;
import com.example.william.my.module.router.ARouterPath;

import java.io.File;
import java.io.InputStream;

/**
 * 拉伸区域(左上)：设置拉伸区域
 * 内容区域(右下)：识别内容区域
 * Show lock：当鼠标在图片区域的时候显示不可编辑区域
 * Show patches：在编辑区域显示图片拉伸的区域
 * Show bad patches：在编辑区域显示不好的图片拉伸的区域
 * Show content：在预览区域显示图片的内容区域
 */
@Route(path = ARouterPath.OpenSource.OpenSource_NinePatch)
public class NinePatchActivity extends AppCompatActivity {

    private TextView mTextView1, mTextView2;

    private static final String mPath = "http://wlkk-img.weilitoutiao.net/4205c8fa3e550f4f376160f9887916b9.png";
    private static final String mNinePath = "http://wlkk-img.weilitoutiao.net/39d5dc3d712fe2e8e0ed93d3c16a614b.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_activity_nine_patch);

        mTextView1 = findViewById(R.id.text1);
        mTextView2 = findViewById(R.id.text2);

        //NinePatchChunk
        Glide.with(this).asBitmap().load(mNinePath).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                NinePatchDrawable drawable = NinePatchChunk.create9PatchDrawable(NinePatchActivity.this, resource, mNinePath);
                mTextView1.setBackground(drawable);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
        //NinePatchBuilder
        Glide.with(this).asBitmap().load(mPath).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                NinePatchBuilder builder = new NinePatchBuilder(getResources(), resource);
                builder.addXRegion(30, 32);
                NinePatchDrawable drawable = builder.build();
                mTextView2.setBackground(drawable);

                //如果您不想要ninepatch，只想使用区块，请点击此处
                //Here if you don't want ninepatch and only want chunk use
                //NinePatchBuilder builder9 = new NinePatchBuilder(bitmap.getWidth(), bitmap.getHeight());
                //byte[] chunk9 = builder9.addXCenteredRegion(1).addYCenteredRegion(1).buildChunk();
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
    }
}
