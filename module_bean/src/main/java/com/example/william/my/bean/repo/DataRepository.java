package com.example.william.my.bean.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.william.my.bean.api.NetworkService;
import com.example.william.my.bean.data.ArticleDataBean;
import com.example.william.my.core.retrofit.callback.LiveDataCallback;
import com.example.william.my.core.retrofit.callback.RetrofitResponseCallback;
import com.example.william.my.core.retrofit.exception.ApiException;
import com.example.william.my.core.retrofit.response.RetrofitResponse;
import com.example.william.my.core.retrofit.utils.RetrofitUtils;

/**
 * 数据仓库
 * LiveData 是一个生命周期感知组件，它并不属于 Repositories 或者 DataSource 层，最好在 View 和 ViewModel 层中使用它。
 * 如果在 Repositories 或者 DataSource 中使用会有几个问题：
 * 1. 它不支持线程切换，其次不支持背压，也就是在一段时间内发送数据的速度 > 接受数据的速度，LiveData 无法正确的处理这些请求
 * 2. 使用 LiveData 的最大问题是所有数据转换都将在主线程上完成
 */
public class DataRepository implements DataSource {

    private final NetworkService service;

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
        service = RetrofitUtils.buildApi(NetworkService.class);
    }

    public void getArticle(int page, LoadArticleCallback callback) {
        RetrofitUtils.buildResponseSingle(
                service.getArticleResponse(page),
                new RetrofitResponseCallback<ArticleDataBean>() {

                    @Override
                    public void onLoading() {
                        super.onLoading();
                        callback.showLoading();
                    }

                    @Override
                    public void onFailure(ApiException e) {
                        callback.onFailure(e.getMessage());
                    }

                    @Override
                    public void onResponse(ArticleDataBean response) {
                        callback.onArticleLoaded(response.getDatas());
                    }
                });
    }

    public LiveData<RetrofitResponse<ArticleDataBean>> getArticle(int page) {
        final MutableLiveData<RetrofitResponse<ArticleDataBean>> liveData = new MutableLiveData<>();

        RetrofitUtils.buildLiveData(service.getArticleResponse(page), new LiveDataCallback<>(liveData));

        return liveData;
    }

    public LiveData<RetrofitResponse<ArticleDataBean>> getArticleData(int page) {
        final MutableLiveData<RetrofitResponse<ArticleDataBean>> liveData = new MutableLiveData<>();

        LiveDataCallback.LiveDataConvert<ArticleDataBean, ArticleDataBean> convert = new LiveDataCallback.LiveDataConvert<ArticleDataBean, ArticleDataBean>() {
            @Override
            public RetrofitResponse<ArticleDataBean> onResponse(RetrofitResponse<ArticleDataBean> data) throws Exception {
                return RetrofitResponse.success(data.getData());
            }
        };

        RetrofitUtils.buildLiveData(service.getArticleResponse(page), new LiveDataCallback<>(liveData, convert));

        return liveData;
    }
}
