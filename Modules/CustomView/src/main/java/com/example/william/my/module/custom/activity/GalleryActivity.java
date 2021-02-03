package com.example.william.my.module.custom.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.core.widget.transformer.ScaleInTransformer;
import com.example.william.my.module.custom.R;
import com.example.william.my.module.router.ARouterPath;

/**
 * 画廊效果
 * ViewPager及其父控件都设置 android:clipChildren="false"。
 * https://github.com/hongyangAndroid/MagicViewPager
 */
@Route(path = ARouterPath.CustomView.CustomView_Gallery)
public class GalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_activity_gallery);

        FrameLayout mContainer = findViewById(R.id.gallery_container);
        ViewPager mViewPager = findViewById(R.id.gallery_viewPager);

        //设置Page间间距
        mViewPager.setPageMargin(10);
        //设置缓存的页面数量
        mViewPager.setOffscreenPageLimit(3);

        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 10;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                View mView = LayoutInflater.from(GalleryActivity.this).inflate(R.layout.custom_layout_gallery, container, false);
                container.addView(mView);
                return mView;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }
        });

        //mViewPager.setPageTransformer(true, new AlphaPageTransformer());
        mViewPager.setPageTransformer(true, new ScaleInTransformer());
    }
}