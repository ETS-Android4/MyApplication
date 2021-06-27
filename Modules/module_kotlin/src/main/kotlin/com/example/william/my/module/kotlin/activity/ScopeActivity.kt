package com.example.william.my.module.kotlin.activity

import android.os.Bundle
import android.util.Log
import com.example.william.my.library.base.BaseActivity
import com.example.william.my.module.kotlin.data.LoginData
import com.example.william.my.module.kotlin.databinding.KtActivityPagingBinding
import com.google.gson.Gson

/**
 * 作用域函数
 */
class ScopeActivity : BaseActivity() {

    private lateinit var binding: KtActivityPagingBinding

    private var string: String? = null
    private var arrayList: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = KtActivityPagingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun checkNull() {
        string.isNullOrEmpty()
        arrayList.isNullOrEmpty()
    }

    private fun getUserName(): String {
        return string ?: "Anonymous"
    }

    private fun <T1, T2> ifNotNull(value1: T1?, value2: T2?, bothNotNull: (T1, T2) -> (Unit)) {
        if (value1 != null && value2 != null) {
            bothNotNull(value1, value2)
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

        // with
        // 对于这个对象，执行以下操作
        with(loginData) {
            data.id = "004"
            data.nickname = "nickname"
        }

        // let
        // 1. 用于仅使用 "非空" 值执行代码块
        // 2. 引入作用域受限的局部变量以提高代码的可读性。
        loginData.let { login ->
            login.data.id = "001"
            login.data.nickname = "nickname"
        }

        val let2: String = loginData.let { login ->
            login.data.id = "001"
            login.data.nickname = "nickname"
            "let2"
        }
        Log.e(TAG, "let $let2")

        // run
        // 对于这个对象，执行以下操作
        loginData.run {
            data.id = "002"
            data.nickname = "nickname"
        }

        val run2: String = loginData.run {
            data.id = "003"
            data.nickname = "nickname"
            "run2"
        }
        Log.e(TAG, "run $run2")

        // also
        // 上下文对象 作为 lambda 表达式参数（it）来访问。 返回值是上下文对象本身。
        val alsoData: LoginData = loginData.also { login ->
            login.data.id = "005"
            login.data.nickname = "nickname"
        }
        Log.e("TAG", Gson().toJson(alsoData))

        // apply
        // 上下文对象 作为接收者（this）来访问。 返回值 是上下文对象本身。
        val applyData: LoginData = loginData.apply {
            data.id = "006"
            data.nickname = "nickname"
        }
        Log.e("TAG", Gson().toJson(applyData))
    }

    /**
     * 数据类
     */
    data class User(
        var id: String,
        var nickname: String
    )

    /**
     * 密封类
     * 枚举的拓展，不能被实例化，可以有多个实例，子类都必须要内嵌在密封类中
     * 一种专门用来配合 when 语句使用的类，在使用 when 表达式时，不需要 else
     */
    sealed class Expr {
        object NotANumber : Expr()
        data class Const(val number: Double) : Expr()
        data class Sum(val e1: Expr, val e2: Expr) : Expr()
    }

}