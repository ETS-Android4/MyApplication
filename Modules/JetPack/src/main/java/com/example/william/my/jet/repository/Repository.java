package com.example.william.my.jet.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.william.my.core.network.retrofit.callback.LiveDataCallback;
import com.example.william.my.core.network.retrofit.response.RetrofitResponse;
import com.example.william.my.core.network.retrofit.utils.RetrofitUtils;
import com.example.william.my.module.bean.BannerBean;
import com.example.william.my.module.bean.BannerData;
import com.example.william.my.module.service.NetworkService;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据仓库
 */
public class Repository {

    private static Repository repository;

    public static Repository getInstance() {
        if (repository == null) {
            repository = new Repository();
        }
        return repository;
    }

    public LiveData<RetrofitResponse<List<BannerBean>>> bannerBean() {
        final MutableLiveData<RetrofitResponse<List<BannerBean>>> liveData = new MutableLiveData<>();

        RetrofitUtils.buildLiveData(
                RetrofitUtils
                        .buildApi(NetworkService.class)
                        .getBannersResponse(),
                new LiveDataCallback<>(liveData));

        return liveData;
    }

    public LiveData<RetrofitResponse<List<BannerData>>> bannerData() {
        final MutableLiveData<RetrofitResponse<List<BannerData>>> liveData = new MutableLiveData<>();

        LiveDataCallback.LiveDataConvert<List<BannerBean>, List<BannerData>> convert = new LiveDataCallback.LiveDataConvert<List<BannerBean>, List<BannerData>>() {
            @Override
            public RetrofitResponse<List<BannerData>> convert(RetrofitResponse<List<BannerBean>> data) throws Exception {
                List<BannerData> movieData = new ArrayList<>();
                for (BannerBean movieBean : data.getData()) {
                    movieData.add(movieBean.convert());
                }
                return RetrofitResponse.success(movieData);
            }
        };

        RetrofitUtils.buildLiveData(
                RetrofitUtils
                        .buildApi(NetworkService.class)
                        .getBannersResponse(),
                new LiveDataCallback<>(liveData, convert));

        return liveData;
    }
}
