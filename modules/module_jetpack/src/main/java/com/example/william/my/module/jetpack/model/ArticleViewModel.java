package com.example.william.my.module.jetpack.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;
import androidx.paging.PagingSource;
import androidx.paging.rxjava3.PagingRx;

import com.example.william.my.module.bean.ArticleDetailBean;
import com.example.william.my.module.jetpack.source.ArticlePagingSource;

import io.reactivex.rxjava3.core.Flowable;
import kotlin.jvm.functions.Function0;
import kotlinx.coroutines.CoroutineScope;

public class ArticleViewModel extends ViewModel {

    /**
     * Paging Coroutines -> LiveData
     */
    public LiveData<PagingData<ArticleDetailBean>> getArticleLiveData() {
        // CoroutineScope 由 lifecycle-viewmodel-ktx 提供
        // CoroutineScope helper provided by the lifecycle-viewmodel-ktx artifact.
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);

        Pager<Integer, ArticleDetailBean> pager = new Pager<>(
                //一次加载的数目
                new PagingConfig(20),
                new Function0<PagingSource<Integer, ArticleDetailBean>>() {
                    @Override
                    public PagingSource<Integer, ArticleDetailBean> invoke() {
                        return new ArticlePagingSource();
                    }
                });
        // cachedIn() 运算符使数据流可共享
        return PagingLiveData.cachedIn(PagingLiveData.getLiveData(pager), viewModelScope);
    }

    /**
     * Paging RxJava -> Flowable
     */
    public Flowable<PagingData<ArticleDetailBean>> getArticleFlowable() {
        // CoroutineScope 由 lifecycle-viewmodel-ktx 提供
        // CoroutineScope helper provided by the lifecycle-viewmodel-ktx artifact.
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);

        Pager<Integer, ArticleDetailBean> pager = new Pager<>(
                //一次加载的数目
                new PagingConfig(20),
                new Function0<PagingSource<Integer, ArticleDetailBean>>() {
                    @Override
                    public PagingSource<Integer, ArticleDetailBean> invoke() {
                        return new ArticlePagingSource();
                    }
                });

        Flowable<PagingData<ArticleDetailBean>> flowable = PagingRx.getFlowable(pager);
        // cachedIn() 运算符使数据流可共享
        PagingRx.cachedIn(flowable, viewModelScope);
        return flowable;
    }
}
