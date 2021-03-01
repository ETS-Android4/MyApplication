package com.example.william.my.module.wx.viewModel;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class WxViewModel extends ViewModel {

    public final LiveData<Object> mLiveData;

    private final MutableLiveData<String> mMutableLiveData;

    public WxViewModel() {
        mMutableLiveData = new MutableLiveData<>();

        mLiveData = Transformations.switchMap(mMutableLiveData, new Function<String, LiveData<Object>>() {
            @Override
            public LiveData<Object> apply(String input) {
                return new MutableLiveData<>();
            }
        });
    }

    public void callback(String code) {
        mMutableLiveData.postValue(code);
    }
}
