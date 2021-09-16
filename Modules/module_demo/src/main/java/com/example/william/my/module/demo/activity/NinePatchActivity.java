package com.example.william.my.module.demo.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.william.my.module.demo.R;

/**
 * 拉伸区域(左上)：设置拉伸区域
 * 内容区域(右下)：识别内容区域
 * Show lock：当鼠标在图片区域的时候显示不可编辑区域
 * Show patches：在编辑区域显示图片拉伸的区域
 * Show bad patches：在编辑区域显示不好的图片拉伸的区域
 * Show content：在预览区域显示图片的内容区域
 */
public class NinePatchActivity extends AppCompatActivity {

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basics_layout_image);

        mImageView = findViewById(R.id.basics_imageView);
        mImageView.setImageResource(R.mipmap.bubble);
    }

    private Drawable getNinePatchDrawable(Context context, Bitmap bitmap) {
        byte[] chunk = bitmap.getNinePatchChunk();
        NinePatchDrawable drawable = null;
        if (NinePatch.isNinePatchChunk(chunk)) {
            drawable = new NinePatchDrawable(context.getResources(), bitmap, chunk, new Rect(), null);
        }
        return drawable;
    }
}
