package com.example.william.my.sample.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.fragment.PrimaryDarkFragment;
import com.example.william.my.module.fragment.PrimaryFragment;
import com.example.william.my.module.fragment.PrimaryLightFragment;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.sample.R;
import com.example.william.my.sample.adapter.ViewPager2Adapter;
import com.example.william.my.sample.adapter.ViewPager2FragmentAdapter;

import java.util.Arrays;

/**
 * Pages must fill the whole ViewPager2 (use match_parent)
 * ViewPager2内的view布局必须是math_parent，否则会报错
 */
@Route(path = ARouterPath.Sample.Sample_ViewPager2)
public class ViewPager2Activity extends AppCompatActivity {

    private final String[] mData = new String[]{"fragment_primary", "fragment_primary_dark", "fragment_primary_light"};
    private final Fragment[] mFragments = new Fragment[]{new PrimaryFragment(), new PrimaryDarkFragment(), new PrimaryLightFragment()};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_viewpager2);

        ViewPager2 mPageView = findViewById(R.id.page2_view);
        ViewPager2 mPageFragment = findViewById(R.id.page2_fragment);

        mPageView.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mPageView.setAdapter(new ViewPager2Adapter(Arrays.asList(mData)));

        mPageFragment.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);//设置滚动方向
        mPageFragment.setAdapter(new ViewPager2FragmentAdapter(getSupportFragmentManager(), getLifecycle(), Arrays.asList(mFragments)));
    }
}
