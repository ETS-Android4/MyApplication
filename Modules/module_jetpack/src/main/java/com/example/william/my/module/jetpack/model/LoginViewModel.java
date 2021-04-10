package com.example.william.my.module.jetpack.model;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;
import androidx.paging.PagingSource;
import androidx.paging.rxjava3.PagingRx;

import com.example.william.my.core.network.retrofit.response.RetrofitResponse;
import com.example.william.my.module.bean.ArticleBean;
import com.example.william.my.module.bean.BannerDetailBean;
import com.example.william.my.module.bean.BannerDetailData;
import com.example.william.my.module.jetpack.repository.DataRepository;
import com.example.william.my.module.jetpack.source.DataPagingSource;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import kotlin.jvm.functions.Function0;
import kotlinx.coroutines.CoroutineScope;

/**
 * 如果需要Context则使用AndroidViewModel
 * <p>
 * MutableLiveData 可变的，私有，对内访问
 * LiveData 不可变的，对外访问
 * <p>
 * 1.MutableLiveData的父类是LiveData
 * 2.LiveData在实体类里可以通知指定某个字段的数据更新.
 * 3.MutableLiveData则是完全是整个实体类或者数据类型变化后才通知.不会细节到某个字段
 */
public class LoginViewModel extends ViewModel {
//public class LoginViewModel extends AndroidViewModel {

    //public LoginViewModel(@NonNull Application application) {
    //    super(application);
    //}

    private final DataRepository dataRepository;

    // 私有的 MutableLiveData 可变的，对内访问
    private final MutableLiveData<Object> mutableLiveData;

    // 对外暴露 不可变的 LiveData，只能查询
    private final LiveData<RetrofitResponse<List<BannerDetailBean>>> bannersBean;
    private final LiveData<RetrofitResponse<List<BannerDetailData>>> bannersData;

    //private final Flowable<PagingData<ArticlesBean.DataBean.ArticleBean>> articleFlowable;

    public LoginViewModel() {

        dataRepository = DataRepository.getInstance();

        mutableLiveData = new MutableLiveData<>();

        bannersBean = Transformations.switchMap(mutableLiveData, new Function<Object, LiveData<RetrofitResponse<List<BannerDetailBean>>>>() {

            @Override
            public LiveData<RetrofitResponse<List<BannerDetailBean>>> apply(Object input) {
                return dataRepository.bannerBean();
            }
        });
        bannersData = Transformations.switchMap(mutableLiveData, new Function<Object, LiveData<RetrofitResponse<List<BannerDetailData>>>>() {

            @Override
            public LiveData<RetrofitResponse<List<BannerDetailData>>> apply(Object input) {
                return dataRepository.bannerData();
            }
        });
    }

    public LiveData<RetrofitResponse<List<BannerDetailBean>>> getBannersBean() {
        return bannersBean;
    }

    public LiveData<RetrofitResponse<List<BannerDetailData>>> getBannersData() {
        return bannersData;
    }

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

    public void request() {
        mutableLiveData.postValue(true);
    }

}
