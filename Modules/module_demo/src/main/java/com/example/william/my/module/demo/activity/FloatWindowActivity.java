package com.example.william.my.module.demo.activity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.demo.R;
import com.example.william.my.module.router.ARouterPath;

/**
 * 悬浮窗
 */
@Route(path = ARouterPath.Demo.Demo_FloatWindow)
public class FloatWindowActivity extends BaseResponseActivity {

    private View mView;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;

    private boolean isShow;

    @Override
    public void setOnClick() {
        super.setOnClick();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            } else {
                if (!isShow) {
                    showFloatWindow();
                } else {
                    dismissFloatWindow();
                }
            }
        }
    }

    private void showFloatWindow() {
        isShow = true;

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        mLayoutParams = new WindowManager.LayoutParams();

        // 设置宽高
        mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        // 设置背景透明
        mLayoutParams.format = PixelFormat.TRANSPARENT;

        // 设置屏幕左上角为起始点
        mLayoutParams.gravity = Gravity.START | Gravity.TOP;

        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            // 设置窗体显示类型(TYPE_TOAST:与toast一个级别)
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        }
        //添加视图
        mView = LayoutInflater.from(this).inflate(R.layout.demo_layout_float, (ViewGroup) getWindow().getDecorView(), false);

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FloatWindowActivity.this, "您点击了悬浮窗", Toast.LENGTH_SHORT).show();
            }
        });

        mView.setOnTouchListener(new View.OnTouchListener() {

            int startX, startY;  //起始点
            boolean isPerformClick;  //是否点击

            int finalMoveX;  //最后通过动画将mView的X轴坐标移动到finalMoveX

            final int mTouchSlop = ViewConfiguration.get(FloatWindowActivity.this).getScaledTouchSlop();

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getX();
                        startY = (int) event.getY();
                        isPerformClick = true;
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //判断是CLICK还是MOVE
                        //只要移动过，就认为不是点击
                        if (Math.abs(startX - event.getX()) >= mTouchSlop || Math.abs(startY - event.getY()) >= mTouchSlop) {
                            isPerformClick = false;
                        }

                        mLayoutParams.x = (int) (event.getRawX() - startX);
                        mLayoutParams.y = (int) (event.getRawY() - startY);

                        updateFloatWindow();
                        return true;
                    case MotionEvent.ACTION_UP:
                        if (isPerformClick) {
                            mView.performClick();
                        }

                        //判断mView是在Window中的位置，以中间为界
                        if (mLayoutParams.x + mView.getMeasuredWidth() / 2 >= getResources().getDisplayMetrics().widthPixels / 2) {
                            finalMoveX = getResources().getDisplayMetrics().widthPixels - mView.getMeasuredWidth();
                        } else {
                            finalMoveX = 0;
                        }

                        stickToSide();

                        return !isPerformClick;
                    default:
                        break;
                }
                return false;
            }

            private void stickToSide() {
                ValueAnimator animator = ValueAnimator.ofInt(mLayoutParams.x, finalMoveX).setDuration(Math.abs(mLayoutParams.x - finalMoveX));
                animator.setInterpolator(new LinearInterpolator());
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mLayoutParams.x = (int) animation.getAnimatedValue();
                        updateFloatWindow();
                    }
                });
                animator.start();
            }
        });

        mWindowManager.addView(mView, mLayoutParams);
    }

    private void updateFloatWindow() {
        if (mView != null && mLayoutParams != null) {
            mWindowManager.updateViewLayout(mView, mLayoutParams);
        }
    }


    private void dismissFloatWindow() {
        if (mView != null && mWindowManager != null) {
            isShow = false;
            mWindowManager.removeView(mView);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isShow) {
            dismissFloatWindow();
        }
    }
}