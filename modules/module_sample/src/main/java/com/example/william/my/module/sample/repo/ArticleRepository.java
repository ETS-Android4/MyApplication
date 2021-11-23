package com.example.william.my.module.sample.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.example.william.my.bean.data.ArticleDataBean;
import com.example.william.my.core.retrofit.callback.LiveDataCallback;
import com.example.william.my.core.retrofit.callback.ObserverCallback;
import com.example.william.my.core.retrofit.exception.ApiException;
import com.example.william.my.core.retrofit.response.RetrofitResponse;
import com.example.william.my.core.retrofit.utils.RetrofitUtils;
import com.example.william.my.module.sample.api.ArticleService;

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

    /**
     * MVP
     *
     * @param page
     * @param callback
     */
    @Override
    public void getArticleList(int page, LoadArticleCallback callback) {

        RetrofitUtils.buildObservable(
                service.getArticleListCache(page),
                new ObserverCallback<RetrofitResponse<ArticleDataBean>>() {

                    @Override
                    public void onLoading() {
                        super.onLoading();
                        callback.showLoading();
                    }

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
     * MVVM
     *
     * @param page
     * @return
     */
    public LiveData<RetrofitResponse<ArticleDataBean>> getArticleData(int page) {
        final MutableLiveData<RetrofitResponse<ArticleDataBean>> liveData = new MutableLiveData<>();

        RetrofitUtils.buildLiveData(
                service.getArticleList(page),
                new LiveDataCallback<>(liveData));

        return liveData;
    }
}
