package com.example.william.my.module.sample.model;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.william.my.core.network.retrofit.response.RetrofitResponse;
import com.example.william.my.module.sample.bean.ArticleBean;
import com.example.william.my.module.sample.bean.ArticleDetailBean;
import com.example.william.my.module.sample.repo.ArticlesRepository;

import java.util.List;

public class ArticlesViewModel extends ViewModel {

    private int mPage;

    private final ArticlesRepository articlesRepository;

    private final MutableLiveData<Integer> mutableArticleList;

    private final LiveData<RetrofitResponse<List<ArticleDetailBean>>> mObservableArticleList;

    private final MutableLiveData<Integer> mutableArticle;

    private final LiveData<RetrofitResponse<ArticleBean>> mObservableArticle;

    public ArticlesViewModel() {

        articlesRepository = ArticlesRepository.getInstance();

        mutableArticleList = new MutableLiveData<>();

        mObservableArticleList = Transformations.switchMap(mutableArticleList, new Function<Integer, LiveData<RetrofitResponse<List<ArticleDetailBean>>>>() {
            @Override
            public LiveData<RetrofitResponse<List<ArticleDetailBean>>> apply(Integer input) {
                return articlesRepository.getArticleList(input);
            }
        });

        mutableArticle = new MutableLiveData<>();

        mObservableArticle = Transformations.switchMap(mutableArticle, new Function<Integer, LiveData<RetrofitResponse<ArticleBean>>>() {
            @Override
            public LiveData<RetrofitResponse<ArticleBean>> apply(Integer input) {
                return articlesRepository.getArticle(input);
            }
        });
    }

    /**
     * Expose the LiveData Comments query so the UI can observe it.
     */
    public LiveData<RetrofitResponse<List<ArticleDetailBean>>> getArticleList() {
        return mObservableArticleList;
    }

    public void queryArticleList() {
        mPage = 0;
        mutableArticleList.postValue(mPage);
    }

    public void loadArticleList() {
        mPage++;
        mutableArticleList.postValue(mPage);
    }

    public boolean isFirst() {
        return mPage == 0;
    }

    public LiveData<RetrofitResponse<ArticleBean>> getArticle() {
        return mObservableArticle;
    }

    public void queryArticle() {
        mPage = 0;
        mutableArticle.postValue(mPage);
    }

    public void loadArticle() {
        mPage++;
        mutableArticle.postValue(mPage);
    }
}
