package com.example.william.my.module.jetpack.activity;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.jetpack.R;
import com.example.william.my.module.jetpack.databinding.JetActivityBindBinding;
import com.example.william.my.module.jetpack.databinding.JetActivityBindLiveDataBinding;
import com.example.william.my.module.jetpack.databinding.JetActivityBindObservableBinding;
import com.example.william.my.module.jetpack.databinding.JetLayoutMergeBinding;
import com.example.william.my.module.jetpack.model.BindLiveDataViewModel;
import com.example.william.my.module.jetpack.model.BindObservableViewModel;
import com.example.william.my.module.router.ARouterPath;

/**
 * 主 Module 也需要添加配置
 * buildFeatures {
 * dataBinding = true
 * }
 * https://developer.android.google.cn/topic/libraries/view-binding
 * https://developer.android.google.cn/topic/libraries/data-binding
 */
@Route(path = ARouterPath.JetPack.JetPack_Bind)
public class BindActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.jet_activity_bind);

        //setViewBinding();

        //setDataBindingByLiveData();

        setDataBindingByObservable();
    }

    /**
     * ViewBinding
     */
    private void setViewBinding() {
        JetActivityBindBinding mBinding = JetActivityBindBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mBinding.bindTextView.setText("ViewBinding");

        // 带 merge 标签的include不能使用ID，否则会找不到View报空指针异常
        JetLayoutMergeBinding mMergeBinding = JetLayoutMergeBinding.bind(mBinding.getRoot());
        mMergeBinding.mergeTextView.setText("ViewBinding Merge");
    }

    /**
     * DataBinding -> BindLiveDataViewModel -> LiveData
     */
    private void setDataBindingByLiveData() {
        // Obtain ViewModel from ViewModelProviders
        BindLiveDataViewModel mViewModel = new ViewModelProvider(this).get(BindLiveDataViewModel.class);

        // Inflate view and obtain an instance of the binding class.
        JetActivityBindLiveDataBinding mDataBinding = DataBindingUtil.setContentView(this, R.layout.jet_activity_bind_live_data);

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

        JetActivityBindObservableBinding mObsDataBinding = DataBindingUtil.setContentView(this, R.layout.jet_activity_bind_observable);

        mObsDataBinding.setLifecycleOwner(this);

        mObsDataBinding.setModel(mObsViewModel);
    }
}