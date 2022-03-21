package com.example.william.my.module.sample.model;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.william.my.bean.data.ArticleDataBean;
import com.example.william.my.bean.repo.ArticleRepository;
import com.example.william.my.core.retrofit.response.RetrofitResponse;

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


    public void request() {
        mMutableLiveData.postValue(0);
    }
}
