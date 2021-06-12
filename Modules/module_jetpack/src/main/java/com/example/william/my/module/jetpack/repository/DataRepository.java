package com.example.william.my.module.jetpack.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.william.my.core.retrofit.callback.LiveDataCallback;
import com.example.william.my.core.retrofit.response.RetrofitResponse;
import com.example.william.my.core.retrofit.utils.RetrofitUtils;
import com.example.william.my.module.api.NetworkService;
import com.example.william.my.module.bean.BannerDetailBean;
import com.example.william.my.module.bean.BannerDetailData;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据仓库
 * LiveData 是一个生命周期感知组件，它并不属于 Repositories 或者 DataSource 层，最好在 View 和 ViewModel 层中使用它。
 * 如果在 Repositories 或者 DataSource 中使用会有几个问题：
 * 1. 它不支持线程切换，其次不支持背压，也就是在一段时间内发送数据的速度 > 接受数据的速度，LiveData 无法正确的处理这些请求
 * 2. 使用 LiveData 的最大问题是所有数据转换都将在主线程上完成
 */
public class DataRepository {

    private static DataRepository sInstance;

    public static DataRepository getInstance() {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository();
                }
            }
        }
        return sInstance;
    }

    private DataRepository() {

    }

    public LiveData<RetrofitResponse<List<BannerDetailBean>>> bannerBean() {
        final MutableLiveData<RetrofitResponse<List<BannerDetailBean>>> liveData = new MutableLiveData<>();

        RetrofitUtils.buildLiveData(
                RetrofitUtils
                        .buildApi(NetworkService.class)
                        .getBannerList(),
                new LiveDataCallback<>(liveData));

        return liveData;
    }

    public LiveData<RetrofitResponse<List<BannerDetailData>>> bannerData() {
        final MutableLiveData<RetrofitResponse<List<BannerDetailData>>> liveData = new MutableLiveData<>();

        LiveDataCallback.LiveDataConvert<List<BannerDetailBean>, List<BannerDetailData>> convert = new LiveDataCallback.LiveDataConvert<List<BannerDetailBean>, List<BannerDetailData>>() {
            @Override
            public RetrofitResponse<List<BannerDetailData>> onResponse(@NonNull RetrofitResponse<List<BannerDetailBean>> data) throws Exception {
                List<BannerDetailData> movieData = new ArrayList<>();
                for (BannerDetailBean movieBean : data.getData()) {
                    movieData.add(movieBean.convert());
                }
                return RetrofitResponse.success(movieData);
            }
        };

        RetrofitUtils.buildLiveData(
                RetrofitUtils
                        .buildApi(NetworkService.class)
                        .getBannerList(),
                new LiveDataCallback<>(liveData, convert));

        return liveData;
    }
}
