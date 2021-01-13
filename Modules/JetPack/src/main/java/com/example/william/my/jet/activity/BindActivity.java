package com.example.william.my.jet.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.jet.R;
import com.example.william.my.jet.databinding.JetActivityBindBinding;
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

    //private LiveDataViewModel mViewModel;

    private ObservableViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.jet_activity_bind);

        //ViewBinding
        //JetActivityBindBinding binding = JetActivityBindBinding.inflate(getLayoutInflater());
        //View view = binding.getRoot();
        //setContentView(view);

        //binding.bindTextView.setText("ViewBinding");

        // 带 merge 标签的include不能使用ID，否则会找不到View报空指针异常
        //JetLayoutMergeBinding mergeBind = JetLayoutMergeBinding.bind(view);
        //mergeBind.mergeTextView.setText("ViewBinding Merge");

        // DataBinding -> LiveDataViewModel
        // Obtain ViewModel from ViewModelProviders
        //mViewModel = ActivityDataBus.getData(this, LiveDataViewModel.class);

        // DataBinding -> ObservableViewModel
        // An alternative ViewModel using Observable fields and @Bindable properties can be used:
        mViewModel = ActivityDataBus.getData(this, ObservableViewModel.class);

        // Inflate view and obtain an instance of the binding class.
        JetActivityBindBinding dataBinding = DataBindingUtil.setContentView(this, R.layout.jet_activity_bind);

        // Specify the current activity as the lifecycle owner.
        dataBinding.setLifecycleOwner(this);

        // Assign the component to a property in the binding class.
        dataBinding.setModel(mViewModel);
    }

    public void onLike(View v) {
        mViewModel.onLike();
    }
}