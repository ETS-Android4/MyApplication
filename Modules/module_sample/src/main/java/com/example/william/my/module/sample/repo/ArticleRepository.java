package com.example.william.my.module.sample.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.example.william.my.core.retrofit.callback.LiveDataCallback;
import com.example.william.my.core.retrofit.callback.ObserverCallback;
import com.example.william.my.core.retrofit.exception.ApiException;
import com.example.william.my.core.retrofit.observer.RetrofitObserver;
import com.example.william.my.core.retrofit.response.RetrofitResponse;
import com.example.william.my.core.retrofit.utils.RetrofitUtils;
import com.example.william.my.module.bean.ArticleDataBean;
import com.example.william.my.module.bean.ArticleDetailBean;
import com.example.william.my.module.sample.api.ArticleService;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

public class ArticleRepository implements ArticleDataSource {

    private final ArticleService service;

    private static ArticleRepository INSTANCE;

    public static ArticleRepository getInstance() {
        if (INSTANCE == null) {
            synchronized (ArticleRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ArticleRepository();
                }
            }
        }
        return INSTANCE;
    }

    private ArticleRepository() {
        service = RetrofitUtils.buildApi(ArticleService.class);
    }

    @Override
    public void getArticleList(int page, LoadArticleCallback callback) {
        RetrofitUtils.buildObservable(service.getArticleListCache(page), new ObserverCallback<RetrofitResponse<ArticleDataBean>>() {
            @Override
            public void onResponse(@NonNull RetrofitResponse<ArticleDataBean> response) {
                if (ObjectUtils.isNotEmpty(response.getData()) &&
                        CollectionUtils.isNotEmpty(response.getData().getDatas())) {
                    callback.onArticleLoaded(response.getData().getDatas());
                } else {
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(@NonNull ApiException e) {
                callback.onFailure(e.getMessage());
            }
        });
    }

    /**
     * List<ArticleDetailBean>
     */
    public LiveData<RetrofitResponse<List<ArticleDetailBean>>> getArticleList(int page) {
        final MutableLiveData<RetrofitResponse<List<ArticleDetailBean>>> liveData = new MutableLiveData<>();

        LiveDataCallback.LiveDataConvert<ArticleDataBean, List<ArticleDetailBean>> convert = new LiveDataCallback.LiveDataConvert<ArticleDataBean, List<ArticleDetailBean>>() {
            @Override
            public RetrofitResponse<List<ArticleDetailBean>> onResponse(@NonNull RetrofitResponse<ArticleDataBean> data) throws Exception {
                if (ObjectUtils.isNotEmpty(data.getData()) &&
                        CollectionUtils.isNotEmpty(data.getData().getDatas())) {
                    return RetrofitResponse.success(data.getData().getDatas());
                } else {
                    return RetrofitResponse.error(data.getMessage());
                }
            }
        };

        RetrofitUtils.buildLiveData(
                service.getArticleList(page),
                new LiveDataCallback<>(liveData, convert));

        return liveData;
    }

    /**
     * ArticleBean
     */
    public LiveData<RetrofitResponse<ArticleDataBean>> getArticle(int page) {
        final MutableLiveData<RetrofitResponse<ArticleDataBean>> liveData = new MutableLiveData<>();

        RetrofitUtils.buildLiveData(
                service.getArticleList(page),
                new LiveDataCallback<>(liveData));

        return liveData;
    }
}
