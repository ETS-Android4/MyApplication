package com.example.william.my.module.sample.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.example.william.my.core.network.retrofit.callback.LiveDataCallback;
import com.example.william.my.core.network.retrofit.exception.ApiException;
import com.example.william.my.core.network.retrofit.observer.RetrofitObserver;
import com.example.william.my.core.network.retrofit.response.RetrofitResponse;
import com.example.william.my.core.network.retrofit.utils.RetrofitUtils;
import com.example.william.my.module.sample.bean.ArticleBean;
import com.example.william.my.module.sample.bean.ArticleDetailBean;
import com.example.william.my.module.sample.service.ArticleService;

import java.util.List;

public class ArticlesRepository implements ArticlesDataSource {

    private final ArticleService service;

    private static ArticlesRepository INSTANCE;

    private MutableLiveData<List<ArticleDetailBean>> mObservableArticles;

    public static ArticlesRepository getInstance() {
        if (INSTANCE == null) {
            synchronized (ArticlesRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ArticlesRepository();
                }
            }
        }
        return INSTANCE;
    }

    private ArticlesRepository() {
        service = RetrofitUtils.buildApi(ArticleService.class);
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getArticleList(int page, LoadArticleCallback callback) {

        RetrofitUtils.buildObs(
                service.getArticleList(page))
                .subscribe(new RetrofitObserver<RetrofitResponse<ArticleBean>>() {
                    @Override
                    public void onResponse(RetrofitResponse<ArticleBean> response) {
                        if (ObjectUtils.isNotEmpty(response.getData()) &&
                                CollectionUtils.isNotEmpty(response.getData().getDatas())) {
                            callback.onArticleLoaded(response.getData().getDatas());
                        } else {
                            callback.onDataNotAvailable();
                        }
                    }

                    @Override
                    public void onFailure(ApiException e) {
                        callback.onFailure(e.getMessage());
                    }
                });
    }

    /**
     * List<ArticleDetailBean>
     */
    public LiveData<RetrofitResponse<List<ArticleDetailBean>>> getArticleList(int page) {
        final MutableLiveData<RetrofitResponse<List<ArticleDetailBean>>> liveData = new MutableLiveData<>();

        LiveDataCallback.LiveDataConvert<ArticleBean, List<ArticleDetailBean>> convert = new LiveDataCallback.LiveDataConvert<ArticleBean, List<ArticleDetailBean>>() {
            @Override
            public RetrofitResponse<List<ArticleDetailBean>> convert(RetrofitResponse<ArticleBean> data) throws Exception {
                List<ArticleDetailBean> articleList = data.getData().getDatas();
                return RetrofitResponse.success(articleList);
            }
        };

        RetrofitUtils.buildLiveData(
                service.getArticleList(page),
                new LiveDataCallback<>(liveData, convert));

        return liveData;
    }
}
