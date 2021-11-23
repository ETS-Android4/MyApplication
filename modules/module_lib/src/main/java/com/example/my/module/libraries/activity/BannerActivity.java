package com.example.my.module.libraries.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.my.module.libraries.R;
import com.example.my.module.libraries.adapter.SensorAdapter;
import com.example.william.my.core.banner.Banner;
import com.example.william.my.core.banner.adapter.BannerImageAdapter;
import com.example.william.my.core.banner.holder.BannerImageHolder;
import com.example.william.my.core.banner.indicator.NumIndicator;
import com.example.william.my.module.router.ARouterPath;

import java.util.Arrays;

@Route(path = ARouterPath.Lib.Lib_Banner)
public class BannerActivity extends AppCompatActivity {

    private Banner<String, SensorAdapter> mSensorBanner;
    private Banner<String, BannerImageAdapter<String>> mImageBanner;
    private Banner<String, BannerImageAdapter<String>> mGalleryBanner;

    private final String[] mImagesNet = new String[]{
            "https://img.zcool.cn/community/013de756fb63036ac7257948747896.jpg",
            "https://img.zcool.cn/community/01639a56fb62ff6ac725794891960d.jpg",
            "https://img.zcool.cn/community/01270156fb62fd6ac72579485aa893.jpg",
            "https://img.zcool.cn/community/01233056fb62fe32f875a9447400e1.jpg",
            "https://img.zcool.cn/community/016a2256fb63006ac7257948f83349.jpg"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lib_activity_banner);
        mSensorBanner = findViewById(R.id.banner1);
        mSensorBanner.setAdapter(new SensorAdapter(Arrays.asList(mImagesNet)))
                .addBannerLifecycleObserver(this)//添加生命周期观察者
                .setIndicator(new NumIndicator(this));

        mImageBanner = findViewById(R.id.banner2);
        mImageBanner.setAdapter(new BannerImageAdapter<String>(Arrays.asList(mImagesNet)) {

            @Override
            public void onBindView(BannerImageHolder holder, String data, int position, int size) {

            }
        })
                .setBannerRound(20)
                .addBannerLifecycleObserver(this)//添加生命周期观察者
                .setIndicator(new NumIndicator(this));
        //.setIndicator(new CircleIndicator(this));
        //.setIndicator(new RectangleIndicator(this));
        //.setIndicator(new RoundLinesIndicator(this));

        mGalleryBanner = findViewById(R.id.banner3);
        mGalleryBanner.setAdapter(new BannerImageAdapter<String>(Arrays.asList(mImagesNet)) {

            @Override
            public void onBindView(BannerImageHolder holder, String data, int position, int size) {

            }
        })
                .setBannerGallery(2, 0)
                .addBannerLifecycleObserver(this)//添加生命周期观察者
                .setIndicator(new NumIndicator(this));
    }
}