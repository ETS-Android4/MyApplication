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
