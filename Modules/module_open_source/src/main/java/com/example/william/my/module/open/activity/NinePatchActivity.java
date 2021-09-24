package com.example.william.my.module.open.activity;

import android.graphics.Bitmap;
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
import com.example.william.my.module.open.R;
import com.example.william.my.module.router.ARouterPath;

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

    private TextView mTextView;

    private static final String mNinePath = "http://wlkk-img.weilitoutiao.net/39d5dc3d712fe2e8e0ed93d3c16a614b.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_activity_nine_patch);

        mTextView = findViewById(R.id.text1);
        Glide.with(this).asBitmap().load(mNinePath).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                NinePatchDrawable drawable = NinePatchChunk.create9PatchDrawable(NinePatchActivity.this, resource, mNinePath);
                mTextView.setBackground(drawable);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
    }
}
