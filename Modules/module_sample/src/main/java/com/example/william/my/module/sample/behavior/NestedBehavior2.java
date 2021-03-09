package com.example.william.my.module.sample.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.william.my.core.widget.utils.SizeUtils;

public class NestedBehavior2 extends CoordinatorLayout.Behavior<View> {

    int offsetTotal = SizeUtils.dp2px(70);
    private TextView contentView;
    private View imageView;

    public NestedBehavior2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 会遍历每一个 子View，询问它们是否对滚动列表的滚动事件感兴趣，若 Behavior.onStartNestedScroll 方法返回 true，
     * 则表示感兴趣，那么滚动列表后续的滚动事件都会分发到该 子View的Behavior
     */
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int axes, int type) {
        return true;
    }

    /**
     * 处理 子View 的滚动事件
     */
    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        if (contentView == null || imageView == null) {
            //contentView = child.findViewById(R.id.toolbar_tv_title);
            //imageView = child.findViewById(R.id.toolbar_btn_back);
        }
        offset(child, target.getScrollY());
    }

    public void offset(View child, int dy) {
        int alpha = 0;
        if (dy >= offsetTotal) {
            alpha = 255;
            contentView.setSelected(true);
            imageView.setSelected(true);
        } else if (dy == 0) {
            alpha = 0;
        } else {
            alpha = (int) (dy * 255f / offsetTotal);
            contentView.setSelected(false);
            imageView.setSelected(false);
        }
        child.getBackground().setAlpha(alpha);


    }

}
