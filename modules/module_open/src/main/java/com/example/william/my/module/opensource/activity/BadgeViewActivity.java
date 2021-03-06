package com.example.william.my.module.opensource.activity;

import android.view.View;

import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.opensource.R;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.utils.T;

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
                        T.show("已读");
                    }
                });
    }
}