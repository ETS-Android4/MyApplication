package com.example.william.my.module.opensource.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.opensource.R;
import com.example.william.my.module.router.ARouterPath;
import com.github.chrisbanes.photoview.PhotoView;

/**
 * https://github.com/chrisbanes/PhotoView
 */
@Route(path = ARouterPath.OpenSource.OpenSource_PhotoView)
public class PhotoViewActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_activity_photo_view);

        PhotoView photoView = findViewById(R.id.photoView);
        photoView.setImageResource(R.drawable.ic_launcher);
    }
}
