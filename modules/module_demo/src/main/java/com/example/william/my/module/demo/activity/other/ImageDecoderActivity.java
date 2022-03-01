package com.example.william.my.module.demo.activity.other;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.ImageDecoder.Source;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.demo.R;
import com.example.william.my.module.router.ARouterPath;

import java.io.IOException;

@Route(path = ARouterPath.Demo.Demo_ImageDecoder)
public class ImageDecoderActivity extends AppCompatActivity {

    private ImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basics_layout_image);

        mImageView = findViewById(R.id.basics_imageView);

        //loadImageByBitmapFactory();

        loadImageByImageDecoder();
    }

    private void loadImageByBitmapFactory() {
//        // 从文件中加载
//        Drawable drawable = Drawable.createFromPath("pathName");
        // 从asset中加载
        Drawable drawable2 = null;
        try {
            drawable2 = Drawable.createFromStream(getAssets().open("icons/ic_launcher.png"), "");
        } catch (IOException e) {
            e.printStackTrace();
        }

//        // 从文件中加载
//        Bitmap bitmap = BitmapFactory.decodeFile("pathName");
        // 从asset中加载
        Bitmap bitmap2 = null;
        try {
            bitmap2 = BitmapFactory.decodeStream(getAssets().open("icons/ic_launcher.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        // 从 byte array中加载
//        Bitmap bitmap3 = BitmapFactory.decodeByteArray(data, offset, length, opts);

        //显示Drawable
        mImageView.setImageDrawable(drawable2);
        // 显示 bitmap
        mImageView.setImageBitmap(bitmap2);
    }

    private void loadImageByImageDecoder() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            Source source = ImageDecoder.createSource(getResources(), R.drawable.ic_launcher);

            Source source2 = ImageDecoder.createSource(getAssets(), "icons/ic_launcher.png");

            Bitmap decodeBitmap = null;
            Drawable decodeDrawable = null;
            try {
                decodeBitmap = ImageDecoder.decodeBitmap(source2);
                decodeDrawable = ImageDecoder.decodeDrawable(source2);
            } catch (IOException e) {
                e.printStackTrace();
            }

            mImageView.setImageBitmap(decodeBitmap);
            mImageView.setImageDrawable(decodeDrawable);
        }
    }
}