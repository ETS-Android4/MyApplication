package com.example.william.my.core.retrofit.callback;


import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.william.my.core.retrofit.exception.ApiException;
import com.example.william.my.core.retrofit.response.RetrofitResponse;
import com.example.william.my.core.retrofit.status.State;

/**
 * 携带状态 {@link State} 的 LiveData
 */
public class LiveDataCallback<Bean, Data> implements RetrofitResponseCallback<RetrofitResponse<Bean>> {

    public LiveDataConvert<Bean, Data> convert;
    private final MutableLiveData<RetrofitResponse<Data>> liveData;

    /**
     * @param liveData Data
     */
    public LiveDataCallback(MutableLiveData<RetrofitResponse<Data>> liveData) {
        this.liveData = liveData;
        onLoading();
    }

    /**
     * @param liveData Data
     */
    public LiveDataCallback(MutableLiveData<RetrofitResponse<Data>> liveData, LiveDataConvert<Bean, Data> convert) {
        this.liveData = liveData;
        this.convert = convert;
        onLoading();
    }

    @Override
    public void onLoading() {
        this.liveData.postValue(RetrofitResponse.loading());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onResponse(@NonNull RetrofitResponse<Bean> data) {
        try {
            liveData.postValue(convert == null ? (RetrofitResponse<Data>) data : convert.onResponse(data));
        } catch (Exception e) {
            liveData.postValue(RetrofitResponse.error("数据异常"));
        }
    }

    @Override
    public void onFailure(@NonNull ApiException e) {
        liveData.postValue(RetrofitResponse.error(e.getMessage()));
    }

    public interface LiveDataConvert<Bean, Data> {
        RetrofitResponse<Data> onResponse(@NonNull RetrofitResponse<Bean> data) throws Exception;
    }
}
