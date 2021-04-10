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

import com.example.william.my.module.bean.ArticleBean;
import com.example.william.my.module.jetpack.source.DataPagingSource;

import io.reactivex.rxjava3.core.Flowable;
import kotlin.jvm.functions.Function0;
import kotlinx.coroutines.CoroutineScope;

public class ArticleViewModel extends ViewModel {

    /**
     * Paging Coroutines -> LiveData
     */
    public LiveData<PagingData<ArticleBean.DataBean.ArticleDetailBean>> getArticleLiveData() {
        // CoroutineScope 由 lifecycle-viewmodel-ktx 提供
        // CoroutineScope helper provided by the lifecycle-viewmodel-ktx artifact.
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);

        Pager<Integer, ArticleBean.DataBean.ArticleDetailBean> pager = new Pager<>(
                new PagingConfig(20),//一次加载的数目
                new Function0<PagingSource<Integer, ArticleBean.DataBean.ArticleDetailBean>>() {
                    @Override
                    public PagingSource<Integer, ArticleBean.DataBean.ArticleDetailBean> invoke() {
                        return new DataPagingSource();
                    }
                });
        // cachedIn() 运算符使数据流可共享
        return PagingLiveData.cachedIn(PagingLiveData.getLiveData(pager), viewModelScope);
    }

    /**
     * Paging RxJava -> Flowable
     */
    public Flowable<PagingData<ArticleBean.DataBean.ArticleDetailBean>> getArticleFlowable() {
        // CoroutineScope 由 lifecycle-viewmodel-ktx 提供
        // CoroutineScope helper provided by the lifecycle-viewmodel-ktx artifact.
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);

        Pager<Integer, ArticleBean.DataBean.ArticleDetailBean> pager = new Pager<>(
                new PagingConfig(20),//一次加载的数目
                new Function0<PagingSource<Integer, ArticleBean.DataBean.ArticleDetailBean>>() {
                    @Override
                    public PagingSource<Integer, ArticleBean.DataBean.ArticleDetailBean> invoke() {
                        return new DataPagingSource();
                    }
                });

        Flowable<PagingData<ArticleBean.DataBean.ArticleDetailBean>> flowable = PagingRx.getFlowable(pager);
        // cachedIn() 运算符使数据流可共享
        PagingRx.cachedIn(flowable, viewModelScope);
        return flowable;
    }
}
