package com.example.william.my.module.sample.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.example.william.my.core.widget.utils.SizeUtils;

public class WantNestedBehavior extends CoordinatorLayout.Behavior<View> {
    int totleBarTotal = SizeUtils.dp2px(190);
    int offsetTotal = SizeUtils.dp2px(380);
    private TextView contentView;
    private View imageView, shareView, layoutView;

    public WantNestedBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 会遍历每一个 子View，询问它们是否对滚动列表的滚动事件感兴趣，若 Behavior.onStartNestedScroll 方法返回 true，
     * 则表示感兴趣，那么滚动列表后续的滚动事件都会分发到该 子View的Behavior
     */
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int axes, int type) {
        //return target.getId() == R.id.basicres_nested_behavior;
        return true;
    }

    /**
     * 处理 子View 的滚动事件
     */
    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        if (contentView == null || imageView == null || shareView == null) {
            ViewGroup viewGroup = (ViewGroup) child.getParent();
            //layoutView = viewGroup.findViewById(R.id.toolbar_layout);
            //contentView = viewGroup.findViewById(R.id.toolbar_tv_title);
            //imageView = viewGroup.findViewById(R.id.toolbar_btn_back);
            //shareView = viewGroup.findViewById(R.id.toolbar_btn_right);
        }

        offset(child, target.getScrollY());
        stopNestedScrollIfNeeded(dy, target.getScrollY(), target, type);
    }


    public void offset(View child, int dy) {
        if (dy >= offsetTotal) {
            child.setVisibility(View.VISIBLE);
        } else {
            child.setVisibility(View.INVISIBLE);
        }

        int alpha = 0;
        if (dy >= totleBarTotal) {
            alpha = 255;
            contentView.setVisibility(View.VISIBLE);
            imageView.setSelected(true);
            shareView.setSelected(true);
        } else if (dy == 0) {
            alpha = 0;
        } else {
            alpha = (int) (dy * 255f / totleBarTotal);
            contentView.setVisibility(View.GONE);
            imageView.setSelected(false);
            shareView.setSelected(false);
        }
        layoutView.getBackground().setAlpha(alpha);
    }


    private void stopNestedScrollIfNeeded(int dy, int currOffset, View target, int type) {
        if (type == ViewCompat.TYPE_NON_TOUCH) {
            if ((dy < 0 && currOffset == 0)
                    || (dy > 0 && currOffset > offsetTotal)) {
                ViewCompat.stopNestedScroll(target, ViewCompat.TYPE_NON_TOUCH);
            }
        }
    }

}
