package com.example.william.my.custom.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.core.widget.gestureLock.GestureLock;
import com.example.william.my.custom.R;
import com.example.william.my.module.router.ARouterPath;

/**
 * 手势密码
 */
@Route(path = ARouterPath.CustomView.CustomView_GestureLock)
public class GestureLockActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_activity_gesture_lock);

        GestureLock mGestureLock = findViewById(R.id.gesture_lock_gestureLock);
        mGestureLock.setAnswer(new int[]{1, 2, 3});
        mGestureLock.setOnGestureLockListener(new GestureLock.OnGestureLockListener() {
            /*
             * 选中item的Id
             */
            @Override
            public void onItemSelected(int cId) {

            }

            /*
             * 是否匹配
             */
            @Override
            public void onGestureEvent(boolean matched) {

            }

            /*
             * 超过尝试次数
             */
            @Override
            public void onExceed() {

            }
        });
    }
}