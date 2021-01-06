package com.example.william.my.core.widget.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.william.my.core.widget.utils.SizeUtils;

public class NestedBehavior extends CoordinatorLayout.Behavior<View> {

    private int targetId;

    private final int offsetTotal = SizeUtils.dp2px(200);

    public NestedBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 确定使用Behavior的View要依赖的View的类型
     */
    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        return super.layoutDependsOn(parent, child, dependency);
    }

    /**
     * 当被依赖的View状态改变时回调
     */
    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        return super.onDependentViewChanged(parent, child, dependency);
    }

    /**
     * 嵌套滑动开始（ACTION_DOWN），确定Behavior是否要监听此次事件
     */
    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        //return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
        return target.getId() == targetId;
    }

    /**
     * 嵌套滑动进行中，要监听的子 View将要滑动，滑动事件即将被消费（但最终被谁消费，可以通过代码控制）
     */
    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);

        int scrollY = target instanceof RecyclerView ? getDistance((RecyclerView) target) : target.getScrollY();
        offset(child, scrollY);
        stopNestedScrollIfNeeded(dy, scrollY, target, type);
    }


    private int getDistance(RecyclerView target) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) target.getLayoutManager();
        View firstVisibilityItem = target.getChildAt(0);
        if (layoutManager != null && layoutManager.findFirstVisibleItemPosition() > 0) {
            return offsetTotal;
        }
        return -firstVisibilityItem.getTop();
    }

    private void offset(View child, int scrollY) {
        int alpha;
        if (scrollY >= offsetTotal) {
            alpha = 255;
        } else {
            alpha = (int) (scrollY * 255f / offsetTotal);
        }
        child.getBackground().setAlpha(alpha);
    }

    private void stopNestedScrollIfNeeded(int dy, int currOffset, View target, int type) {
        if (type == ViewCompat.TYPE_NON_TOUCH) {
            if ((dy < 0 && currOffset == 0) || (dy > 0 && currOffset > offsetTotal)) {
                ViewCompat.stopNestedScroll(target, ViewCompat.TYPE_NON_TOUCH);
            }
        }
    }
}
