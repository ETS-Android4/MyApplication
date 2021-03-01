package com.example.william.my.module.open.activity;

import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.open.R;
import com.example.william.my.module.router.ARouterPath;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * https://github.com/qstumn/BadgeView
 */
@Route(path = ARouterPath.OpenSource.OpenSource_BadgeView)
public class BadgeViewActivity extends BaseResponseActivity {

    @Override
    public void initView() {
        super.initView();

        new QBadgeView(this)
                .bindTarget(mResponse)
                .setBadgeNumber(5)
                .setBadgeBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                .setGravityOffset(48, 48, true)
                .setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                    @Override
                    public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                        Toast.makeText(BadgeViewActivity.this, "已读", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}