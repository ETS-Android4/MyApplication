package com.example.william.my.module.sample.model;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.william.my.bean.data.ArticleDataBean;
import com.example.william.my.core.retrofit.response.RetrofitResponse;
import com.example.william.my.module.sample.repo.ArticleRepository;

public class ArticleViewModel extends ViewModel {

    private int mPage;

    private final ArticleRepository mArticleRepo;

    private final MutableLiveData<Integer> mMutableLiveData;

    private final LiveData<RetrofitResponse<ArticleDataBean>> mObservableArticle;

    public ArticleViewModel() {

        mArticleRepo = ArticleRepository.getInstance();

        mMutableLiveData = new MutableLiveData<>();

        mObservableArticle = Transformations.switchMap(mMutableLiveData, new Function<Integer, LiveData<RetrofitResponse<ArticleDataBean>>>() {
            @Override
            public LiveData<RetrofitResponse<ArticleDataBean>> apply(Integer input) {
                return mArticleRepo.getArticleData(input);
            }
        });
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
        mMutableLiveData.postValue(mPage);
    }

    public void onLoadMoreArticle() {
        mPage++;
        mMutableLiveData.postValue(mPage);
    }
}
