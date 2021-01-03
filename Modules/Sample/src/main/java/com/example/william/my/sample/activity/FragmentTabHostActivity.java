package com.example.william.my.sample.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTabHost;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.fragment.PrimaryDarkFragment;
import com.example.william.my.module.fragment.PrimaryFragment;
import com.example.william.my.module.fragment.PrimaryLightFragment;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.sample.R;

/**
 * onAttach -> onCreate -> onActivityCreated -> onStart -> onResume
 */
@SuppressWarnings("deprecation")
@Route(path = ARouterPath.Sample.Sample_FragmentTabHost)
public class FragmentTabHostActivity extends AppCompatActivity {

    //TabWidget
    private TabWidget mTabs;

    //FragmentTabHost
    private FragmentTabHost mTabHost;

    //Fragment界面数组
    private final Class<?>[] mFragmentArray = {PrimaryFragment.class, PrimaryDarkFragment.class, PrimaryLightFragment.class};

    //存放图片数组
    private final int[] mImageArray = {R.drawable.sample_ic_home_black_24dp, R.drawable.sample_ic_dashboard_black_24dp, R.drawable.sample_ic_notifications_black_24dp};

    //选修卡文字
    private final String[] mTextArray = {"首页", "列表", "消息"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_fragment_tabhost);
        mTabs = findViewById(android.R.id.tabs);
        mTabHost = findViewById(android.R.id.tabhost);

        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        for (int i = 0; i < mFragmentArray.length; i++) {
            // 给每个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextArray[i]).setIndicator(getTabView(mImageArray, mTextArray, i));
            // 将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, mFragmentArray[i], null);

            // 设置Tab按钮的背景
            mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.WHITE);
        }
        //updateTabView();
        //mTabHost.setCurrentTab(0);

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                //在这监听切换，可根据需求做一些特定的操作
                //updateTabView();
            }
        });
    }

    private View getTabView(int[] imageArray, String[] textArray, int i) {
        View view = getLayoutInflater().inflate(R.layout.sample_item_tab_fragment, mTabs, false);
        ImageView imageView = view.findViewById(R.id.item_tab_imageView);
        imageView.setImageResource(imageArray[i]);
        TextView textView = view.findViewById(R.id.item_tab_textView);
        textView.setText(textArray[i]);
        return view;
    }

    private void updateTabView() {
        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
            TextView tv = mTabHost.getTabWidget().getChildAt(i).findViewById(R.id.item_tab_textView);
            if (mTabHost.getCurrentTab() == i) {//选中
                tv.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            } else {//不选中
                tv.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            }
        }
    }
}
