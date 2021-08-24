package com.example.william.my.module.open.activity;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.open.R;
import com.example.william.my.module.router.ARouterPath;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import java.util.Arrays;

/**
 * https://github.com/youth5201314/banner
 */
@Route(path = ARouterPath.OpenSource.OpenSource_Banner)
public class BannerActivity extends BaseActivity {

    //本地图片
    private final Integer[] mImagesLocal = new Integer[]{
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_activity_banner);
        Banner<Integer, BannerImageAdapter<Integer>> banner = findViewById(R.id.banner);
        banner.setAdapter(new BannerImageAdapter<Integer>(Arrays.asList(mImagesLocal)) {

            @Override
            public void onBindView(BannerImageHolder holder, Integer data, int position, int size) {
                holder.imageView.setImageResource(data);
            }
        })
                .addBannerLifecycleObserver(this)//添加生命周期观察者
                .setIndicator(new CircleIndicator(this));
    }
}