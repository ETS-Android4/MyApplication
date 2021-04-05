package com.example.william.my.module.demo.model;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.william.my.core.network.retrofit.response.RetrofitResponse;
import com.example.william.my.module.demo.bean.ArticleBean;
import com.example.william.my.module.demo.bean.ArticleDetailBean;
import com.example.william.my.module.demo.repo.ArticlesRepository;

import java.util.List;

public class ArticlesViewModel extends ViewModel {

    private int mPage;

    private final ArticlesRepository articlesRepository;

    private final MutableLiveData<Integer> mutableLiveData;

    private final LiveData<RetrofitResponse<List<ArticleDetailBean>>> mObservableArticleList;

    //private final LiveData<RetrofitResponse<ArticleBean>> mObservableArticle;

    public ArticlesViewModel() {

        articlesRepository = ArticlesRepository.getInstance();

        mutableLiveData = new MutableLiveData<>();

        mObservableArticleList = Transformations.switchMap(mutableLiveData, new Function<Integer, LiveData<RetrofitResponse<List<ArticleDetailBean>>>>() {
            @Override
            public LiveData<RetrofitResponse<List<ArticleDetailBean>>> apply(Integer input) {
                return articlesRepository.getArticleList(input);
            }
        });
        //mObservableArticle = Transformations.switchMap(mutableLiveData, new Function<Integer, LiveData<RetrofitResponse<ArticleBean>>>() {
        //    @Override
        //    public LiveData<RetrofitResponse<ArticleBean>> apply(Integer input) {
        //        return null;
        //    }
        //});
    }

    /**
     * Expose the LiveData Comments query so the UI can observe it.
     */
    public LiveData<RetrofitResponse<List<ArticleDetailBean>>> getArticleList() {
        return mObservableArticleList;
    }

    public void queryArticleList() {
        mPage = 0;
        mutableLiveData.postValue(mPage);
    }

    public void loadArticleList() {
        mPage++;
        mutableLiveData.postValue(mPage);
    }

    public boolean isFirst() {
        return mPage == 0;
    }
}
