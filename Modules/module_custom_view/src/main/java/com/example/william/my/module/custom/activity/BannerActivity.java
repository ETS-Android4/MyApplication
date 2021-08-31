package com.example.william.my.module.custom.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.core.banner.Banner;
import com.example.william.my.core.banner.adapter.BannerImageAdapter;
import com.example.william.my.core.banner.holder.BannerImageHolder;
import com.example.william.my.module.custom.R;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.CustomView.CustomView_Banner)
public class BannerActivity extends AppCompatActivity {

    private Banner mBanner;

    //本地图片
    private final Integer[] mImagesLocal = new Integer[]{R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_activity_banner);
        mBanner = findViewById(R.id.banner);
//        mBanner.setAdapter(new BannerImageAdapter(Arrays.asList(mImagesLocal)) {
//
//            @Override
//            public void onBindView(BannerImageHolder holder, Object data, int position, int size) {
//                holder.imageView.setImageResource(data);
//            }
//        });
//                .addBannerLifecycleObserver(this)//添加生命周期观察者
//                .setIndicator(new CircleIndicator(this));
    }
}