package com.example.william.my.module.sample.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.module.router.ARouterPath
import com.example.william.my.module.sample.R
import com.example.william.my.module.sample.databinding.SampleLayoutBindRecyclerBinding
import com.example.william.my.module.sample.model.LiveDataVMFactory
import com.example.william.my.module.sample.model.LiveDataViewModel

@Route(path = ARouterPath.Sample.Sample_Kotlin_Bind)
class KtBindActivity : AppCompatActivity() {

    // Obtain ViewModel
    private val mViewModel: LiveDataViewModel by viewModels {
        LiveDataVMFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setContentView(R.layout.sample_layout_recycler)

        // Obtain binding object using the Data Binding library
        val binding = DataBindingUtil.setContentView<SampleLayoutBindRecyclerBinding>(
            this, R.layout.sample_layout_bind_recycler
        )

        // Set the LifecycleOwner to be able to observe LiveData objects
        binding.lifecycleOwner = this

        // Bind ViewModel
        binding.viewModel = mViewModel
    }
}