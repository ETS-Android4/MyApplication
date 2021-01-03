package com.example.william.my.core.network.retrofit.loading;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.william.my.core.network.R;
import com.example.william.my.core.network.utils.BarUtils;

public class LoadingTip extends LinearLayout implements View.OnClickListener {

    private String mMessage;
    private TextView mTextView;

    public LoadingTip(Context context) {
        super(context);
        initView(context);
    }

    public LoadingTip(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LoadingTip(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.basics_layout_loading, this);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setBackgroundColor(Color.WHITE);

        mTextView = findViewById(R.id.loading_textView);
        mTextView.setOnClickListener(this);

        setLoadingTip(Status.loading);
        setOnReloadListener(onReloadListener);
    }

    @Override
    public void onClick(View v) {
        if (onReloadListener != null) {
            onReloadListener.reload();
        }
    }

    public enum Status {
        loading, empty, finish, error
    }

    public void setMessage(String message) {
        mTextView.setText(message);
    }

    public void setLoadingTip(Status status) {
        switch (status) {
            case loading:
                setVisibility(View.VISIBLE);
                setEnabled(false);
                mTextView.setText("加载中……");
                break;
            case empty:
                setVisibility(View.VISIBLE);
                setEnabled(false);
                if (mMessage == null) {
                    mTextView.setText("暂无数据");
                } else {
                    mTextView.setText(mMessage);
                }
                break;
            case finish:
                setVisibility(GONE);
                break;
            case error:
                setVisibility(View.VISIBLE);
                setEnabled(true);
                if (mMessage == null) {
                    mTextView.setText("网络异常，请刷新页面");
                } else {
                    mTextView.setText(mMessage);
                }
                break;
        }
    }

    /**
     * 全屏LoadingTip
     */
    public static LoadingTip addLoadingTipFullScreen(Activity context) {
        LoadingTip loadingTip = new LoadingTip(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(0, context.getResources().getDimensionPixelSize(R.dimen.toolbar_minHeight) + BarUtils.getStatusBarHeight(), 0, 0);
        ((FrameLayout) context.getWindow().getDecorView()).addView(loadingTip);
        return loadingTip;
    }

    /**
     * 带标题栏LoadingTip
     */
    public static LoadingTip addLoadingTipWithTopBar(Activity context) {
        LoadingTip loadingTip = new LoadingTip(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(0, context.getResources().getDimensionPixelSize(R.dimen.toolbar_minHeight) + BarUtils.getStatusBarHeight(), 0, 0);
        ((FrameLayout) context.getWindow().getDecorView()).addView(loadingTip);
        return loadingTip;
    }

    private LoadingTipListener onReloadListener;

    public void setOnReloadListener(LoadingTipListener listener) {
        this.onReloadListener = listener;
    }

    /**
     * 重新尝试接口
     */
    public interface LoadingTipListener {
        void reload();
    }
}
