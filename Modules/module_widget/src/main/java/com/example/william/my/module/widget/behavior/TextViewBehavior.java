package com.example.william.my.module.widget.behavior;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class TextViewBehavior extends CoordinatorLayout.Behavior<View> {

    public TextViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        return dependency instanceof Button;
    }

    /**
     * 当被依赖的View状态改变时回调
     */
    @SuppressLint("SetTextI18n")
    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        child.setX(dependency.getX() + 200);
        child.setY(dependency.getY() + 200);
        ((TextView) child).setText(dependency.getX() + " , " + dependency.getY());
        return true;
    }
}