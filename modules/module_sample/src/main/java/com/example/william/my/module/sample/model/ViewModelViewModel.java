package com.example.william.my.module.sample.model;

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

import com.example.william.my.bean.data.ArticleDataBean;
import com.example.william.my.bean.data.ArticleDetailBean;
import com.example.william.my.bean.repo.ArticleRepository;
import com.example.william.my.core.retrofit.response.RetrofitResponse;
import com.example.william.my.module.sample.source.ArticleRxPagingSource;

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
public class ViewModelViewModel extends ViewModel {
//public class ArticleViewModel extends AndroidViewModel {

    //public ArticleViewModel( Application application) {
    //    super(application);
    //}

    private final MutableLiveData<Integer> mMutableLiveData = new MutableLiveData<>();
    ;

    private final LiveData<RetrofitResponse<ArticleDataBean>> mArticleLiveData;

    public ViewModelViewModel() {
        mArticleLiveData = Transformations.switchMap(mMutableLiveData, new Function<Integer, LiveData<RetrofitResponse<ArticleDataBean>>>() {
            @Override
            public LiveData<RetrofitResponse<ArticleDataBean>> apply(Integer input) {
                return ArticleRepository.getInstance().loadArticle(input);
            }
        });
    }

    /**
     * Expose the LiveData Comments query so the UI can observe it.
     */
    public LiveData<RetrofitResponse<ArticleDataBean>> getArticle() {
        return mArticleLiveData;
    }

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
                        return new ArticleRxPagingSource();
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
                        return new ArticleRxPagingSource();
                    }
                });

        Flowable<PagingData<ArticleDetailBean>> flowable = PagingRx.getFlowable(pager);
        // cachedIn() 运算符使数据流可共享
        PagingRx.cachedIn(flowable, viewModelScope);
        return flowable;
    }

    public void request() {
        mMutableLiveData.postValue(0);
    }
}
