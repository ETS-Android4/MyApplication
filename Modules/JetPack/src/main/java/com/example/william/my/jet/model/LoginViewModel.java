package com.example.william.my.jet.model;

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
import com.example.william.my.jet.repository.Repository;
import com.example.william.my.jet.source.DataPagingSource;
import com.example.william.my.module.bean.ArticlesBean;
import com.example.william.my.module.bean.BannerBean;
import com.example.william.my.module.bean.BannerData;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import kotlin.jvm.functions.Function0;
import kotlinx.coroutines.CoroutineScope;

;

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

    private final Repository repository;

    private final MutableLiveData<Object> mutableLiveData;
    private final LiveData<RetrofitResponse<List<BannerBean>>> bannersBean;
    private final LiveData<RetrofitResponse<List<BannerData>>> bannersData;

    //private final Flowable<PagingData<ArticlesBean.DataBean.ArticleBean>> articleFlowable;

    public LoginViewModel() {

        repository = Repository.getInstance();

        mutableLiveData = new MutableLiveData<>();

        bannersBean = Transformations.switchMap(mutableLiveData, new Function<Object, LiveData<RetrofitResponse<List<BannerBean>>>>() {

            @Override
            public LiveData<RetrofitResponse<List<BannerBean>>> apply(Object input) {
                return repository.bannerBean();
            }
        });
        bannersData = Transformations.switchMap(mutableLiveData, new Function<Object, LiveData<RetrofitResponse<List<BannerData>>>>() {

            @Override
            public LiveData<RetrofitResponse<List<BannerData>>> apply(Object input) {
                return repository.bannerData();
            }
        });


    }

    public LiveData<RetrofitResponse<List<BannerBean>>> getBannersBean() {
        return bannersBean;
    }

    public LiveData<RetrofitResponse<List<BannerData>>> getBannersData() {
        return bannersData;
    }

    public LiveData<PagingData<ArticlesBean.DataBean.ArticleBean>> getArticleLiveData() {
        // CoroutineScope 由 lifecycle-viewmodel-ktx 提供
        // CoroutineScope helper provided by the lifecycle-viewmodel-ktx artifact.
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);

        Pager<Integer, ArticlesBean.DataBean.ArticleBean> pager = new Pager<>(
                new PagingConfig(20),//一次加载的数目
                new Function0<PagingSource<Integer, ArticlesBean.DataBean.ArticleBean>>() {
                    @Override
                    public PagingSource<Integer, ArticlesBean.DataBean.ArticleBean> invoke() {
                        return new DataPagingSource();
                    }
                });
        // cachedIn() 运算符使数据流可共享
        return PagingLiveData.cachedIn(PagingLiveData.getLiveData(pager), viewModelScope);
    }

    public Flowable<PagingData<ArticlesBean.DataBean.ArticleBean>> getArticleFlowable() {
        // CoroutineScope 由 lifecycle-viewmodel-ktx 提供
        // CoroutineScope helper provided by the lifecycle-viewmodel-ktx artifact.
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);

        Pager<Integer, ArticlesBean.DataBean.ArticleBean> pager = new Pager<>(
                new PagingConfig(20),//一次加载的数目
                new Function0<PagingSource<Integer, ArticlesBean.DataBean.ArticleBean>>() {
                    @Override
                    public PagingSource<Integer, ArticlesBean.DataBean.ArticleBean> invoke() {
                        return new DataPagingSource();
                    }
                });
        // cachedIn() 运算符使数据流可共享
        return PagingRx.cachedIn(PagingRx.getFlowable(pager), viewModelScope);
    }

    public void request() {
        mutableLiveData.postValue(true);
    }

}
