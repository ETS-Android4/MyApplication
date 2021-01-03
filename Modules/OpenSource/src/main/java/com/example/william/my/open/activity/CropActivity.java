package com.example.william.my.open.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.open.R;
import com.yalantis.ucrop.UCrop;

/**
 * https://github.com/Yalantis/uCrop
 */
@Route(path = ARouterPath.OpenSource.OpenSource_Crop)
public class CropActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basics_layout_image);

        mImageView = findViewById(R.id.basics_imageView);
        mImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //第一个参数：需要裁剪的图片；第二个参数：裁剪后图片
//        UCrop.of("sourceUri", "destinationUri")
//                .withAspectRatio(1, 1)
//                .withMaxResultSize(1024, 1024)
//                .start(this, 1000);
    }
}