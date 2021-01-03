package com.example.william.my.open.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.open.R;
import com.github.chrisbanes.photoview.PhotoView;

/**
 * https://github.com/chrisbanes/PhotoView
 */
@Route(path = ARouterPath.OpenSource.OpenSource_PhotoView)
public class PhotoViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_activity_photo_view);

        PhotoView photoView = findViewById(R.id.photoView);
        photoView.setImageResource(R.drawable.ic_launcher);
    }
}
