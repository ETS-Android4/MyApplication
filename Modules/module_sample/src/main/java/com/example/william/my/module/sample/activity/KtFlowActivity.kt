package com.example.william.my.module.sample.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.module.router.ARouterPath
import com.example.william.my.module.sample.databinding.SampleLayoutRecyclerBinding
import com.example.william.my.module.sample.model.LiveDataVMFactory
import com.example.william.my.module.sample.model.LiveDataViewModel

@Route(path = ARouterPath.Sample.Sample_Kotlin_Flow)
class KtFlowActivity : AppCompatActivity() {

    // Obtain ViewModel
    private val mViewModel: LiveDataViewModel by viewModels {
        LiveDataVMFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setContentView(R.layout.sample_layout_recycler)

        val binding = SampleLayoutRecyclerBinding.inflate(layoutInflater)

    }
}