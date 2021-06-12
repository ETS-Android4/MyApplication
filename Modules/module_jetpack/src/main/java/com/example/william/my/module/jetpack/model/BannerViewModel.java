package com.example.william.my.module.jetpack.model;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.william.my.core.retrofit.response.RetrofitResponse;
import com.example.william.my.module.bean.BannerDetailBean;
import com.example.william.my.module.bean.BannerDetailData;
import com.example.william.my.module.jetpack.repository.DataRepository;

import java.util.List;

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
public class BannerViewModel extends ViewModel {
//public class LoginViewModel extends AndroidViewModel {

    //public LoginViewModel(@NonNull Application application) {
    //    super(application);
    //}

    private final DataRepository mRepository;

    /**
     * 私有的 MutableLiveData 可变的，对内访问
     */
    private final MutableLiveData<Object> mMutableLiveData;

    /**
     * 对外暴露 不可变的 LiveData，只能查询
     */
    private final LiveData<RetrofitResponse<List<BannerDetailBean>>> bannerBean;
    private final LiveData<RetrofitResponse<List<BannerDetailData>>> bannerData;

    public BannerViewModel() {

        mRepository = DataRepository.getInstance();

        mMutableLiveData = new MutableLiveData<>();

        bannerBean = Transformations.switchMap(mMutableLiveData, new Function<Object, LiveData<RetrofitResponse<List<BannerDetailBean>>>>() {

            @Override
            public LiveData<RetrofitResponse<List<BannerDetailBean>>> apply(Object input) {
                return mRepository.bannerBean();
            }
        });
        bannerData = Transformations.switchMap(mMutableLiveData, new Function<Object, LiveData<RetrofitResponse<List<BannerDetailData>>>>() {

            @Override
            public LiveData<RetrofitResponse<List<BannerDetailData>>> apply(Object input) {
                return mRepository.bannerData();
            }
        });
    }

    public LiveData<RetrofitResponse<List<BannerDetailBean>>> getBannerBean() {
        return bannerBean;
    }

    public LiveData<RetrofitResponse<List<BannerDetailData>>> getBannerData() {
        return bannerData;
    }

    public void request() {
        mMutableLiveData.postValue(true);
    }

}
