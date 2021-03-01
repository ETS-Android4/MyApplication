package com.example.william.my.module.jetpack.repository;

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
 * LiveData 是一个生命周期感知组件，它并不属于 Repositories 或者 DataSource 层，最好在 View 和 ViewModel 层中使用它。
 * 如果在 Repositories 或者 DataSource 中使用会有几个问题：
 * 1. 它不支持线程切换，其次不支持背压，也就是在一段时间内发送数据的速度 > 接受数据的速度，LiveData 无法正确的处理这些请求
 * 2. 使用 LiveData 的最大问题是所有数据转换都将在主线程上完成
 */
public class Repository {

    private static Repository repository;

    public static Repository getInstance() {
        if (repository == null) {
            repository = new Repository();
        }
        return repository;
    }

    private Repository() {

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
