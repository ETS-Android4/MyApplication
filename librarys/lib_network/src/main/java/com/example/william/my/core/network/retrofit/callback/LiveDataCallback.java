package com.example.william.my.core.network.retrofit.callback;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.william.my.core.network.retrofit.exception.ApiException;
import com.example.william.my.core.network.retrofit.response.RetrofitResponse;
import com.example.william.my.core.network.retrofit.status.State;

/**
 * 携带状态 {@link State} 的 LiveData
 */
public class LiveDataCallback<Bean, Data> implements RetrofitCallback<RetrofitResponse<Bean>> {

    public LiveDataConvert<Bean, Data> convert;
    private final MutableLiveData<RetrofitResponse<Data>> liveData;

    /**
     * @param liveData Data
     */
    public LiveDataCallback(MutableLiveData<RetrofitResponse<Data>> liveData) {
        this.liveData = liveData;
        this.liveData.postValue(RetrofitResponse.loading());
    }

    /**
     * @param liveData Data
     */
    public LiveDataCallback(MutableLiveData<RetrofitResponse<Data>> liveData, LiveDataConvert<Bean, Data> convert) {
        this.liveData = liveData;
        this.liveData.postValue(RetrofitResponse.loading());
        this.convert = convert;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onResponse(RetrofitResponse<Bean> data) {
        try {
            liveData.postValue(convert == null ? (RetrofitResponse<Data>) data : convert.convert(data));
        } catch (Exception e) {
            e.printStackTrace();
            liveData.postValue(RetrofitResponse.error("数据异常"));
        }
    }

    @Override
    public void onFailure(@NonNull ApiException e) {
        liveData.postValue(RetrofitResponse.error(e.getMessage()));
    }

    public interface LiveDataConvert<Bean, Data> {
        RetrofitResponse<Data> convert(RetrofitResponse<Bean> data) throws Exception;
    }
}
