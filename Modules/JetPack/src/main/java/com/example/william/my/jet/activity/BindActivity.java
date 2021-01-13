package com.example.william.my.jet.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.jet.R;
import com.example.william.my.jet.databinding.JetActivityBindObsBinding;
import com.example.william.my.jet.model.ObservableViewModel;
import com.example.william.my.library.utils.ActivityDataBus;
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
public class BindActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.jet_activity_bind);

        //ViewBinding
//        JetActivityBindBinding mBinding = JetActivityBindBinding.inflate(getLayoutInflater());
//        setContentView(mBinding.getRoot());
//
//        mBinding.bindTextView.setText("ViewBinding");
//
//        // 带 merge 标签的include不能使用ID，否则会找不到View报空指针异常
//        JetLayoutMergeBinding mMergeBinding = JetLayoutMergeBinding.bind(mBinding.getRoot());
//        mMergeBinding.mergeTextView.setText("ViewBinding Merge");

        // DataBinding -> LiveDataViewModel
//        // Obtain ViewModel from ViewModelProviders
//        LiveDataViewModel mViewModel = ActivityDataBus.getData(this, LiveDataViewModel.class);
//
//        // Inflate view and obtain an instance of the binding class.
//        JetActivityBindBinding mDataBinding = DataBindingUtil.setContentView(this, R.layout.jet_activity_bind);
//
//        // Specify the current activity as the lifecycle owner.
//        mDataBinding.setLifecycleOwner(this);
//
//        // Assign the component to a property in the binding class.
//        mDataBinding.setModel(mViewModel);

        // DataBinding -> ObservableViewModel
        // An alternative ViewModel using Observable fields and @Bindable properties can be used:
        ObservableViewModel mObsViewModel = ActivityDataBus.getData(this, ObservableViewModel.class);
        JetActivityBindObsBinding mObsDataBinding = DataBindingUtil.setContentView(this, R.layout.jet_activity_bind_obs);
        mObsDataBinding.setLifecycleOwner(this);
        mObsDataBinding.setModel(mObsViewModel);
    }
}