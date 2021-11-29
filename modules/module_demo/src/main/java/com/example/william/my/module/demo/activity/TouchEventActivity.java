package com.example.william.my.module.demo.activity;

import android.os.Bundle;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.demo.R;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.utils.L;

/**
 * 1. 点击 View1 区域且事件被 View1 消费        => View onTouchEvent 返回 true
 * <p>
 * Activity      [老板]: dispatchTouchEvent     把按钮做的好看一点,要有光泽,给人一种点击的欲望.
 * ViewGroupA    [经理]: dispatchTouchEvent     技术部,老板说按钮不好看,要加一道光.
 * ViewGroupA    [经理]: onInterceptTouchEvent
 * ViewB         [码农]: dispatchTouchEvent     加一道光.
 * ViewB         [码农]: onTouchEvent           做好了.
 * <p>
 * 如果事件被消费，就意味着事件信息传递终止。
 * <p>
 * 2. 点击 View1 区域但没有任何 View 消费事件    => View onTouchEvent 返回 false
 * <p>
 * Activity      [老板]: dispatchTouchEvent     经理,我准备发展一下电商业务,下周之前做一个淘宝出来.
 * ViewGroupA    [经理]: dispatchTouchEvent     技术部,老板要做淘宝,下周上线.
 * ViewGroupA    [经理]: onInterceptTouchEvent
 * ViewB         [码农]: dispatchTouchEvent     做淘宝???
 * ViewB         [码农]: onTouchEvent           这个真心做不了啊
 * ViewGroupA    [经理]: onTouchEvent           报告老板, 技术部说做不了
 * Activity      [老板]: onTouchEvent           这么简单都做不了,你们都是干啥的(愤怒).
 * <p>
 * 如果事件一直没有被消费，最后会传给Activity，如果Activity也不需要就被抛弃。
 * <p>
 * 3. 点击 View1 区域但事件被 ViewGroupA 拦截
 * <p>
 * Activity      [老板]: dispatchTouchEvent     现在项目做到什么程度了?
 * ViewGroupA    [经理]: dispatchTouchEvent     技术部,你们的app快做完了么?
 * ViewGroupA    [经理]: onInterceptTouchEvent
 * ViewGroupA    [经理]: onTouchEvent           正在测试,明天就测试完了
 * <p>
 * 判断事件是否被消费是根据返回值，而不是根据你是否使用了事件。
 */
@Route(path = ARouterPath.Demo.Demo_TouchEvent)
public class TouchEventActivity extends AppCompatActivity {

    private static final String TAG = "Activity     [老板]";

    public static int plan = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity_touch_event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (plan == 1) {
                L.e(TAG, "dispatchTouchEvent     " + "把按钮做的好看一点,要有光泽,给人一种点击的欲望.");
            } else if (plan == 2) {
                L.e(TAG, "dispatchTouchEvent     " + "经理,我准备发展一下电商业务,下周之前做一个淘宝出来.");
            } else if (plan == 3) {
                L.e(TAG, "dispatchTouchEvent     " + "现在项目做到什么程度了?");
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (plan == 2) {
                L.e(TAG, "onTouchEvent           " + "这么简单都做不了,你们都是干啥的(愤怒).");
            }
        }
        return super.onTouchEvent(event);
    }
}