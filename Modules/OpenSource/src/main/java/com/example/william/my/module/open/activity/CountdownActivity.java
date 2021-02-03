package com.example.william.my.module.open.activity;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.open.R;
import com.example.william.my.module.router.ARouterPath;

import cn.iwgang.countdownview.CountdownView;

/**
 * https://github.com/iwgang/CountdownView
 */
@Route(path = ARouterPath.OpenSource.OpenSource_Countdown)
public class CountdownActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_activity_countdown);

        CountdownView mCvCountdownView = findViewById(R.id.countdownView);

        mCvCountdownView.start(995550000); // Millisecond

        for (int time = 0; time < 1000; time++) {
            mCvCountdownView.updateShow(time);
        }
    }
}
