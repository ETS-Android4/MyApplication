package com.example.william.my.core.retrofit.observer;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.example.william.my.core.retrofit.loading.LoadingTip;
import com.example.william.my.core.retrofit.response.RetrofitResponse;
import com.example.william.my.core.retrofit.status.State;

import java.util.Collection;

/**
 * androidx.lifecycle.Observer
 */
public abstract class WithLoadingTipObserver<T> implements Observer<RetrofitResponse<T>> {

    private LoadingTip mLoadingTip;

    public WithLoadingTipObserver() {
    }

    public WithLoadingTipObserver(LoadingTip loadingTip) {
        this.mLoadingTip = loadingTip;
    }

    public WithLoadingTipObserver(LoadingTip loadingTip, String message) {
        this.mLoadingTip = loadingTip;
        this.mLoadingTip.setMessage(message);
    }

    @Override
    public void onChanged(@NonNull RetrofitResponse<T> tRetrofitResponse) {
        switch (tRetrofitResponse.getCode()) {
            case State.LOADING:
                if (mLoadingTip != null) {
                    mLoadingTip.setLoadingTip(LoadingTip.Status.loading);
                }
                break;
            case State.SUCCESS:
                if (mLoadingTip != null) {
                    mLoadingTip.setLoadingTip(isEmpty(tRetrofitResponse.getData()) ? LoadingTip.Status.empty : LoadingTip.Status.finish);
                }
                onResponse(tRetrofitResponse.getData());
                break;
            default:
                if (!onFailure(tRetrofitResponse.getMessage())) {
                    if (mLoadingTip != null) {
                        mLoadingTip.setLoadingTip(LoadingTip.Status.error);
                    }
                }
                break;
        }
    }

    @SuppressWarnings("rawtypes")
    private boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        }
        if (object instanceof Collection) {
            return ((Collection) object).isEmpty();
        }
        return false;
    }

    /**
     * State.SUCCESS 时，返回 response
     *
     * @param response
     */
    protected abstract void onResponse(@NonNull T response);

    /**
     * State.ERROR 时，返回 错误信息
     *
     * @return false 显示默认提示
     */
    @SuppressWarnings("SameReturnValue")
    protected boolean onFailure(String msg) {
        return false;
    }
}