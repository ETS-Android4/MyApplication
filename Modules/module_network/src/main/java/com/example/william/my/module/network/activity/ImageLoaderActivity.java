package com.example.william.my.module.network.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.core.imageloader.ImageLoader;
import com.example.william.my.core.imageloader.glide.transformation.RadiusTransformation.CornerType;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.network.R;
import com.example.william.my.module.router.ARouterPath;


@Route(path = ARouterPath.NetWork.NetWork_ImageLoader)
public class ImageLoaderActivity extends BaseActivity {

    private int type;
    private ImageView mImageView;
    private static final String URL_IMG = "https://web.hycdn.cn/arknights/official/pic/20210401/8b683b7c01ebf0eb570370a48b655504.png";

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
                        ImageLoader.getInstance().load(ImageLoaderActivity.this, URL_IMG, mImageView);
                        break;
                    case 2:
                        ImageLoader.getInstance().loadCircle(ImageLoaderActivity.this, URL_IMG, mImageView);
                        break;
                    case 3:
                        ImageLoader.getInstance().loadRadius(ImageLoaderActivity.this, URL_IMG, mImageView, 48);
                        break;
                    case 4:
                        ImageLoader.getInstance().loadRadius(ImageLoaderActivity.this, URL_IMG, mImageView, 48, CornerType.TOP);
                        break;
                    default:
                        type = 0;
                        break;
                }
            }
        });
    }

}