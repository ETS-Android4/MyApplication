package com.example.william.my.core.network.retrofit.callback;

import androidx.lifecycle.MutableLiveData;

import com.example.william.my.core.network.retrofit.exception.ApiException;
import com.example.william.my.core.network.retrofit.response.RetrofitResponse;

public class LiveDataCallback<Bean, Data> implements RetrofitCallback<RetrofitResponse<Bean>> {

    public LiveDataConvert<Bean, Data> convert;
    private final MutableLiveData<RetrofitResponse<Data>> liveData;

    /**
     * @param liveData Data
     */
    public LiveDataCallback(MutableLiveData<RetrofitResponse<Data>> liveData) {
        this.liveData = liveData;
        this.liveData.postValue(RetrofitResponse.<Data>loading());
    }

    /**
     * @param liveData Data
     */
    public LiveDataCallback(MutableLiveData<RetrofitResponse<Data>> liveData, LiveDataConvert<Bean, Data> convert) {
        this.liveData = liveData;
        this.liveData.postValue(RetrofitResponse.<Data>loading());
        this.convert = convert;
    }

    @Override
    public void onResponse(RetrofitResponse<Bean> data) {
        try {
            //noinspection unchecked
            liveData.postValue(convert == null ? (RetrofitResponse<Data>) data : convert.convert(data));
        } catch (Exception e) {
            e.printStackTrace();
            liveData.postValue(RetrofitResponse.<Data>error("数据异常"));
        }
    }

    @Override
    public void onFailure(ApiException e) {
        liveData.postValue(RetrofitResponse.<Data>error(e.getMessage()));
    }

    public interface LiveDataConvert<Bean, Data> {
        RetrofitResponse<Data> convert(RetrofitResponse<Bean> data) throws Exception;
    }
}
