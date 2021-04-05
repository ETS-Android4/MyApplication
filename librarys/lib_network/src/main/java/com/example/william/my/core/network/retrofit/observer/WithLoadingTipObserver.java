package com.example.william.my.core.network.retrofit.observer;

import androidx.lifecycle.Observer;

import com.example.william.my.core.network.retrofit.loading.LoadingTip;
import com.example.william.my.core.network.retrofit.response.RetrofitResponse;
import com.example.william.my.core.network.retrofit.status.State;

import java.util.Collection;

/**
 * androidx.lifecycle.Observer
 */
public abstract class WithLoadingTipObserver<T> implements Observer<RetrofitResponse<T>> {

    private final String mMessage;

    private LoadingTip mLoadingTip;

    public WithLoadingTipObserver() {
        this.mMessage = "";
    }

    public WithLoadingTipObserver(LoadingTip loadingTip) {
        this.mMessage = "";
        this.mLoadingTip = loadingTip;
    }

    public WithLoadingTipObserver(LoadingTip onProgressDialog, String mMessage) {
        this.mMessage = mMessage;
        this.mLoadingTip = onProgressDialog;
    }

    @Override
    public void onChanged(RetrofitResponse<T> tRetrofitResponse) {
        switch (tRetrofitResponse.getCode()) {
            case State.LOADING:
                break;
            case State.SUCCESS:
                if (mLoadingTip != null) {
                    mLoadingTip.setLoadingTip(isEmpty(tRetrofitResponse.getData()) ? LoadingTip.Status.empty : LoadingTip.Status.finish);
                }
                callback(tRetrofitResponse.getData());
                break;
            default:
                if (!onFail(tRetrofitResponse.getMessage())) {
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

    protected abstract void callback(T response);

    /**
     * @return false 显示默认提示
     */
    @SuppressWarnings("SameReturnValue")
    public boolean onFail(String msg) {
        return false;
    }
}
