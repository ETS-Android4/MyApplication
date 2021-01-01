package com.example.william.my.jet.model;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.william.my.core.network.retrofit.response.RetrofitResponse;
import com.example.william.my.jet.repository.Repository;
import com.example.william.my.module.bean.BannerBean;
import com.example.william.my.module.bean.BannerData;

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
public class LoginViewModel extends ViewModel {
//public class LoginViewModel extends AndroidViewModel {

    //public LoginViewModel(@NonNull Application application) {
    //    super(application);
    //}

    private final Repository repository;

    private final MutableLiveData<Object> mutableLiveData;
    private final LiveData<RetrofitResponse<List<BannerBean>>> bannersBean;
    private final LiveData<RetrofitResponse<List<BannerData>>> bannersData;

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

    public void request() {
        mutableLiveData.postValue(true);
    }

}
