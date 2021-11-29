package com.example.william.my.module.sample.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.module.router.ARouterPath
import com.example.william.my.module.sample.databinding.SampleLayoutResponseBinding
import com.example.william.my.module.sample.model.SuspendViewModel

@Route(path = ARouterPath.Sample.Sample_Suspend)
class SuspendActivity : AppCompatActivity() {

    lateinit var mViewModel: SuspendViewModel

    lateinit var mBinding: SampleLayoutResponseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = SampleLayoutResponseBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mViewModel = ViewModelProvider(this)[SuspendViewModel::class.java]

        login()
    }

    private fun login() {
        mViewModel.login.observe(this, Observer {
            mBinding.textView.text = it
        })
        mViewModel.login("17778060027", "ww123456")
    }
}