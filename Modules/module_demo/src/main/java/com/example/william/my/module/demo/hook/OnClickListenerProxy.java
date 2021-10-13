package com.example.william.my.module.demo.hook;

import android.util.Log;
import android.view.View;

/**
 * View.OnClickListener代理类，用来做点击后的后续处理
 */
public class OnClickListenerProxy implements View.OnClickListener {

    private final View.OnClickListener mOriginalListener;

    //直接在构造函数中传进来原来的OnClickListener
    public OnClickListenerProxy(View.OnClickListener originalListener) {
        mOriginalListener = originalListener;
    }

    @Override
    public void onClick(View v) {
        Log.e("TAG", "点击事件被hook到了");
        if (mOriginalListener != null) {
            mOriginalListener.onClick(v);
        }
    }
}