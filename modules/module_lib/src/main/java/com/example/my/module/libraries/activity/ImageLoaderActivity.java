package com.example.my.module.libraries.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.my.module.libraries.R;
import com.example.william.my.bean.base.Urls;
import com.example.william.my.core.imageloader.ImageLoader;
import com.example.william.my.core.imageloader.corner.CornerType;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.Lib.Lib_ImageLoader)
public class ImageLoaderActivity extends BaseActivity {

    private int type;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basics_layout_image);

        mImageView = findViewById(R.id.basics_imageView);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type++;
                switch (type) {
                    case 1:
                        ImageLoader.getInstance().load(ImageLoaderActivity.this, Urls.Url_Image, mImageView);
                        break;
                    case 2:
                        ImageLoader.getInstance().loadCircle(ImageLoaderActivity.this, Urls.Url_Image, mImageView);
                        break;
                    case 3:
                        ImageLoader.getInstance().loadRadius(ImageLoaderActivity.this, Urls.Url_Image, mImageView, 48);
                        break;
                    case 4:
                        ImageLoader.getInstance().loadRadius(ImageLoaderActivity.this, Urls.Url_Image, mImageView, 48, CornerType.TOP);
                        break;
                    case 5:
                        ImageLoader.getInstance().loadBlur(ImageLoaderActivity.this, Urls.Url_Image, mImageView);
                    default:
                        type = 0;
                        break;
                }
            }
        });
    }
}