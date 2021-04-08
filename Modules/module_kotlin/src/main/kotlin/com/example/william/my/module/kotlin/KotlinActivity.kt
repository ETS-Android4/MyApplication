package com.example.william.my.module.kotlin

import android.os.Bundle
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.library.base.BaseActivity
import com.example.william.my.module.kotlin.data.LoginData
import com.example.william.my.module.kotlin.databinding.KActivityKotlinBinding
import com.example.william.my.module.kotlin.utils.Singleton
import com.example.william.my.module.router.ARouterPath
import com.google.gson.Gson

/**
 * https://developer.android.google.cn/kotlin/ktx
 */
@Route(path = ARouterPath.Kotlin.Kotlin)
class KotlinActivity : BaseActivity() {

    private lateinit var binding: KActivityKotlinBinding

    private var string: String? = null
    private var arrayList: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = KActivityKotlinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.k_activity_kotlin)

        string.isNullOrEmpty()
        arrayList.isNullOrEmpty()

        scope()

        binding.kotlinTextView.setOnClickListener {
            Singleton.getInstance(application).showToast("Singleton")
        }
    }

    /**
     * 作用域函数
     * run 与 let 会将最后一行传给下个extension function或是回传；
     * also 和 apply 则是将「自己(this)」回传或传入下个extension function
     * <p>
     * 对一个非空（non-null）对象执行 lambda 表达式：let
     * 将表达式作为变量引入为局部作用域中：let
     * 一个对象的一组函数调用：with
     * 对象配置并且计算结果：run
     * 在需要表达式的地方运行语句：非扩展的 run
     * <p>
     * run ： 是 let , with 两个函数结合体
     * also ： 该对象执行以下操作
     * apply : 将以下赋值操作应用于该对象
     */
    private fun scope() {
        val loginData = LoginData(LoginData.User("001", "nickname"))
        // let
        // 1. 用于仅使用非空值执行代码块
        // 2. 引入作用域受限的局部变量以提高代码的可读性。
        val let: Unit = loginData.let { login ->
            login.user.id = "001"
            login.user.nickname = "nickname"
        }

        val let2: String = loginData.let { login ->
            login.user.id = "001"
            login.user.nickname = "nickname"
            "let2"
        }
        Log.e(TAG, "let $let2")

        // with
        // 对于这个对象，执行以下操作
        val with: Unit = with(loginData) {
            user.id = "004"
            user.nickname = "nickname"
        }

        // run
        // 对于这个对象，执行以下操作
        val run: Unit = loginData.run {
            user.id = "002"
            user.nickname = "nickname"
        }

        val run2: String = loginData.run {
            user.id = "003"
            user.nickname = "nickname"
            "run2"
        }
        Log.e(TAG, "run $run2")

        // also
        // 上下文对象 作为 lambda 表达式参数（it）来访问。 返回值是上下文对象本身。
        val alsoData: LoginData = loginData.also { login ->
            login.user.id = "005"
            login.user.nickname = "nickname"
        }
        Log.e("TAG", Gson().toJson(alsoData))

        // apply
        // 上下文对象 作为接收者（this）来访问。 返回值 是上下文对象本身。
        val applyData: LoginData = loginData.apply {
            user.id = "006"
            user.nickname = "nickname"
        }
        Log.e("TAG", Gson().toJson(applyData))
    }

    private fun <T1, T2> ifNotNull(value1: T1?, value2: T2?, bothNotNull: (T1, T2) -> (Unit)) {
        if (value1 != null && value2 != null) {
            bothNotNull(value1, value2)
        }
    }

    /**
     * ?:
     */
    fun getUserName(): String {
        return string ?: "Anonymous"
    }
}

