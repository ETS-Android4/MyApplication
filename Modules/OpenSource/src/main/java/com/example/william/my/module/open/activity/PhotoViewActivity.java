package com.example.william.my.module.open.activity;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.open.R;
import com.example.william.my.module.router.ARouterPath;
import com.github.chrisbanes.photoview.PhotoView;

/**
 * https://github.com/chrisbanes/PhotoView
 */
@Route(path = ARouterPath.OpenSource.OpenSource_PhotoView)
public class PhotoViewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_activity_photo_view);

        PhotoView photoView = findViewById(R.id.photoView);
        photoView.setImageResource(R.drawable.im_ic_launcher);
    }
}
