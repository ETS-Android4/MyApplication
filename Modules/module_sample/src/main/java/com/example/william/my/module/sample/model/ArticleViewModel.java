package com.example.william.my.module.sample.model;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.william.my.core.network.retrofit.response.RetrofitResponse;
import com.example.william.my.module.bean.ArticleDataBean;
import com.example.william.my.module.bean.ArticleDetailBean;
import com.example.william.my.module.sample.repo.ArticleRepository;

import java.util.List;

public class ArticleViewModel extends ViewModel {

    private int mPage;

    private final ArticleRepository articleRepository;

    private final MutableLiveData<Integer> mutableArticleList;

    private final LiveData<RetrofitResponse<List<ArticleDetailBean>>> mObservableArticleList;

    private final MutableLiveData<Integer> mutableArticle;

    private final LiveData<RetrofitResponse<ArticleDataBean>> mObservableArticle;

    public ArticleViewModel() {

        articleRepository = ArticleRepository.getInstance();

        mutableArticleList = new MutableLiveData<>();

        mObservableArticleList = Transformations.switchMap(mutableArticleList, new Function<Integer, LiveData<RetrofitResponse<List<ArticleDetailBean>>>>() {
            @Override
            public LiveData<RetrofitResponse<List<ArticleDetailBean>>> apply(Integer input) {
                return articleRepository.getArticleList(input);
            }
        });

        mutableArticle = new MutableLiveData<>();

        mObservableArticle = Transformations.switchMap(mutableArticle, new Function<Integer, LiveData<RetrofitResponse<ArticleDataBean>>>() {
            @Override
            public LiveData<RetrofitResponse<ArticleDataBean>> apply(Integer input) {
                return articleRepository.getArticle(input);
            }
        });
    }

    /**
     * Expose the LiveData Comments query so the UI can observe it.
     */
    public LiveData<RetrofitResponse<List<ArticleDetailBean>>> getArticleList() {
        return mObservableArticleList;
    }

    public void onRefreshArticleList() {
        mPage = 0;
        mutableArticleList.postValue(mPage);
    }

    public void onLoadMoreArticleList() {
        mPage++;
        mutableArticleList.postValue(mPage);
    }

    public boolean isFirst() {
        return mPage == 0;
    }

    /**
     * Expose the LiveData Comments query so the UI can observe it.
     */
    public LiveData<RetrofitResponse<ArticleDataBean>> getArticle() {
        return mObservableArticle;
    }

    public void onRefreshArticle() {
        mPage = 0;
        mutableArticle.postValue(mPage);
    }

    public void onLoadMoreArticle() {
        mPage++;
        mutableArticle.postValue(mPage);
    }
}
