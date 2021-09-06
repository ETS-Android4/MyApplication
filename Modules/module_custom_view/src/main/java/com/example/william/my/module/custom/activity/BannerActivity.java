package com.example.william.my.module.custom.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.core.banner.Banner;
import com.example.william.my.core.banner.adapter.BannerImageAdapter;
import com.example.william.my.core.banner.holder.BannerImageHolder;
import com.example.william.my.core.banner.indicator.RoundLinesIndicator;
import com.example.william.my.core.imageloader.ImageLoader;
import com.example.william.my.module.custom.R;
import com.example.william.my.module.router.ARouterPath;

import java.util.Arrays;

@Route(path = ARouterPath.CustomView.CustomView_Banner)
public class BannerActivity extends AppCompatActivity {

    private Banner<String, BannerImageAdapter<String>> mBanner;

    private final String[] mImagesNet = new String[]{"https://img.zcool.cn/community/013de756fb63036ac7257948747896.jpg", "https://img.zcool.cn/community/01639a56fb62ff6ac725794891960d.jpg", "https://img.zcool.cn/community/01270156fb62fd6ac72579485aa893.jpg", "https://img.zcool.cn/community/01233056fb62fe32f875a9447400e1.jpg", "https://img.zcool.cn/community/016a2256fb63006ac7257948f83349.jpg",};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_activity_banner);
        mBanner = findViewById(R.id.banner);
        mBanner.setAdapter(new BannerImageAdapter<String>(Arrays.asList(mImagesNet)) {

            @Override
            public void onBindView(BannerImageHolder holder, String data, int position, int size) {
                ImageLoader.getInstance().load(BannerActivity.this, data, holder.imageView);
            }
        }).addBannerLifecycleObserver(this)//添加生命周期观察者
                //.setBannerGallery(48, 60)
                //.setIndicator(new NumIndicator(this));
                //.setIndicator(new CircleIndicator(this));
                .setIndicator(new RoundLinesIndicator(this));
    }
}