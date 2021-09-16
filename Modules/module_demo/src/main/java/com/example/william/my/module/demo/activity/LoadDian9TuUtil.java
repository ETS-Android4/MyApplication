package com.example.william.my.module.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class LoadDian9TuUtil {

    public static void loadDian9Tu(Context context, View imageView, String imgUrl) {
        if (context == null) {
            return;
        }
        Glide.with(context)
                .asFile()
                .load(imgUrl)
                .into(new CustomTarget<File>() {
                    @Override
                    public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                        try {
                            FileInputStream is = new FileInputStream(resource);
                            setNinePathImage(context, imageView, BitmapFactory.decodeStream(is));
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    public static void setNinePathImage(Context context, View imageView, Bitmap bitmap) {
        if (bitmap == null) {
            Log.e("TAG", "bitmap null");
            return;
        }
        byte[] chunk = bitmap.getNinePatchChunk();
        if (NinePatch.isNinePatchChunk(chunk)) {
            NinePatchDrawable patchy = new NinePatchDrawable(context.getResources(), bitmap, chunk, new Rect(), null);
            imageView.setBackground(patchy);
        } else {
            Log.e("TAG", "No NinePatchChunk");
        }
    }
}