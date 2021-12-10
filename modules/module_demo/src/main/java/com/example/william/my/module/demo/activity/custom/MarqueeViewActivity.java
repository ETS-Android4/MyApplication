package com.example.william.my.module.demo.activity.custom;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.core.widget.marqueeview.MarqueeView;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.demo.R;
import com.example.william.my.module.router.ARouterPath;

import java.util.ArrayList;
import java.util.List;

@Route(path = ARouterPath.Demo.Demo_MarqueeView)
public class MarqueeViewActivity extends BaseActivity {

    private final String[] mData = new String[]{"第一条数据", "第二条数据", "第三条数据", "第四条数据"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity_marquee_view);

        MarqueeView mMarqueeView = findViewById(R.id.marquee_marqueeView);

        List<View> marquee_view_views = new ArrayList<>();
        for (int i = 0; i < mData.length; i = i + 2) {
            //设置滚动的单个布局
            LinearLayout viewGroup = (LinearLayout) getLayoutInflater().inflate(R.layout.demo_item_marquee, (ViewGroup) getWindow().getDecorView(), false);
            //初始化布局的控件
            TextView textView1 = viewGroup.findViewById(R.id.item_marquee_primary);
            TextView textView2 = viewGroup.findViewById(R.id.item_marquee_accent);
            //进行对控件赋值
            textView1.setText(mData[i]);
            if (mData.length > i + 1) {
                //因为淘宝那儿是两条数据，但是当数据是奇数时就不需要赋值第二个，所以加了一个判断，还应该把第二个布局给隐藏掉
                textView2.setText(mData[i + 1]);
            } else {
                textView2.setVisibility(View.GONE);
            }
            viewGroup.setGravity(Gravity.CENTER);
            //添加到循环滚动数组里面去
            marquee_view_views.add(viewGroup);
        }
        mMarqueeView.setViews(marquee_view_views);
    }
}