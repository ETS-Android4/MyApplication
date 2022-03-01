package com.example.william.my.module.sample.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.sample.R;
import com.example.william.my.module.sample.databinding.SampleActivityBindBinding;
import com.example.william.my.module.sample.databinding.SampleActivityBindLiveDataBinding;
import com.example.william.my.module.sample.databinding.SampleActivityBindObservableBinding;
import com.example.william.my.module.sample.databinding.SampleLayoutBindMergeBinding;
import com.example.william.my.module.sample.model.BindLiveDataViewModel;
import com.example.william.my.module.sample.model.BindObservableViewModel;

/**
 * 主 Module 也需要添加配置
 * buildFeatures {
 * dataBinding = true
 * }
 * https://developer.android.google.cn/topic/libraries/view-binding
 * https://developer.android.google.cn/topic/libraries/data-binding
 */
@Route(path = ARouterPath.Sample.Sample_Bind)
public class BindActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.jet_activity_bind);

        //setViewBinding();

        setDataBindingByLiveData();

        //setDataBindingByObservable();
    }

    /**
     * ViewBinding
     */
    private void setViewBinding() {
        SampleActivityBindBinding mBinding = SampleActivityBindBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mBinding.bindActivityTextView.setText("ViewBinding");

        // 带 merge 标签的include不能使用ID，否则会找不到View报空指针异常
        SampleLayoutBindMergeBinding mMergeBinding = SampleLayoutBindMergeBinding.bind(mBinding.getRoot());
        mMergeBinding.bindMergeTextView.setText("ViewBinding Merge");
    }

    /**
     * DataBinding -> BindLiveDataViewModel -> LiveData
     */
    private void setDataBindingByLiveData() {
        // Obtain ViewModel from ViewModelProviders
        BindLiveDataViewModel mViewModel = new ViewModelProvider(this).get(BindLiveDataViewModel.class);

        // Inflate view and obtain an instance of the binding class.
        SampleActivityBindLiveDataBinding mDataBinding = DataBindingUtil.setContentView(this, R.layout.sample_activity_bind_live_data);

        // Specify the current activity as the lifecycle owner.
        mDataBinding.setLifecycleOwner(this);

        // Assign the component to a property in the binding class.
        mDataBinding.setModel(mViewModel);
    }

    /**
     * DataBinding -> BindObservableViewModel -> Observable
     */
    private void setDataBindingByObservable() {
        // An alternative ViewModel using Observable fields and @Bindable properties can be used:
        BindObservableViewModel mObsViewModel = new ViewModelProvider(this).get(BindObservableViewModel.class);

        SampleActivityBindObservableBinding mObsDataBinding = DataBindingUtil.setContentView(this, R.layout.sample_activity_bind_observable);

        mObsDataBinding.setLifecycleOwner(this);

        mObsDataBinding.setModel(mObsViewModel);
    }
}