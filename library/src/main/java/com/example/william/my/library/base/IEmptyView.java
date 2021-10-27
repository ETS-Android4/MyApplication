package com.example.william.my.library.base;

import android.view.View;

public interface IEmptyView {

    View getRootView();

    void showEmptyView();

    void showErrorView();

    void hide();

    void setOnClickListener(OnEmptyClickListener listener);

    interface OnEmptyClickListener {
        void onRefresh();
    }
}
