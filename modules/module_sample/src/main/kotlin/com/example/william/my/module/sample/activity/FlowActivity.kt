package com.example.william.my.module.sample.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.library.base.BaseActivity
import com.example.william.my.module.router.ARouterPath
import com.example.william.my.module.sample.databinding.SampleLayoutResponseBinding
import com.example.william.my.module.sample.model.FlowViewModel

/**
 * 数据流
 * https://developer.android.google.cn/kotlin/flow
 */
@Route(path = ARouterPath.Sample.Sample_FLow)
class FlowActivity : BaseActivity() {

    lateinit var mViewModel: FlowViewModel

    private val username = "17778060027"
    private val password = "ww123456"

    lateinit var mBinding: SampleLayoutResponseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = SampleLayoutResponseBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mViewModel = ViewModelProvider(this)[FlowViewModel::class.java]
        login()
    }

    private fun login() {
        mViewModel.login.observe(this, Observer {
            mBinding.textView.text = it
        })
        mViewModel.login(username, password)
    }

    /**
     * 通过 使用 Flow 流构造方法 -> asLiveData() 直接返回 LiveData
     */
    private fun loginByFlow() {
        mViewModel.loginByFLow(username, password).observe(this, Observer {
            mBinding.textView.text = it
        })
    }

    /**
     * 通过 使用 Coroutine 协程构造方法 -> liveData<> 直接返回 LiveData
     */
    private fun loginByCoroutine() {
        mViewModel.loginByCoroutine(username, password).observe(this, Observer {
            mBinding.textView.text = it
        })
    }

    fun flowOperator() {
        /*
         *
        1. 普通操作符

        map
        转换操作符，将 A 变成 B
        take
        后面跟 Int 类型的参数，表示接收多少个 emit 出的值
        filter
        过滤操作符

        2. 特殊的操作符
        总会有一些特殊的情况，比如我只需要取前几个，我只要最新的数据等，不过在这些情况下，数据的发射就是并发执行的。

        buffer
        数据发射并发，collect 不并发

        conflate
        发射数据太快，只处理最新发射的

        collectLatest
        接收处理太慢，只处理最新接收的

        3. 组合操作符

        zip
        组合两个流，双方都有新数据才会发射处理

        combine
        组合两个流，在经过第一次发射以后，任意方有新数据来的时候就可以发射，另一方有可能是已经发射过的数据


        4. 展平流操作符
        展平流有点类似于 RxJava 中的 flatmap，将你发射出去的数据源转变为另一种数据源。

        flatMapConcat
        串行处理数据

        flatMapMerge
        并发 collect 数据

        flatMapLatest
        在每次 emit 新的数据以后，会取消先前的 collect

        5. 末端操作符
        顾名思义，就是帮你做 collect 处理，collect 是最基础的末端操作符。

        collect
        最基础的消费数据

        toList
        转化为 List 集合

        toSet
        转化为 Set 集合

        first
        仅仅取第一个值

        single
        确保流发射单个值

        reduce
        规约，如果发射的是 Int，最终会得到一个 Int，可做累加操作

        fold
        规约，可以说是 reduce 的升级版，可以自定义返回类型
         */
    }
}