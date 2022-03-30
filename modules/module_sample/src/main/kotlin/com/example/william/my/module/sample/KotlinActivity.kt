package com.example.william.my.module.sample

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.william.my.bean.data.LoginBean
import com.example.william.my.library.base.BaseActivity
import com.example.william.my.module.sample.databinding.SampleLayoutResponseBinding
import com.example.william.my.module.sample.utils.Singleton
import com.example.william.my.module.sample.utils.ThreadUtils
import com.example.william.my.module.utils.L
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

class KotlinActivity : BaseActivity() {

    private var string: String? = null
    private var arrayList: ArrayList<String>? = null

    lateinit var mBinding: SampleLayoutResponseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = SampleLayoutResponseBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
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
     * let 与 also -> it
     * run 与 apply -> this
     * <p>
     * let 与 run 会将 "最后一行" 传给下个extension function或是回传；
     * also 和 apply 则是将 "this" 回传或传入下个extension function
     * <p>
     * it 可重命名微一个可读的lambda参数，适合多重嵌套
     */
    private fun scopeTest() {
        val loginData = LoginBean()

        // with
        // 对于这个对象，执行以下操作
        with(loginData) {
            data.id = "001"
            data.nickname = "nickname"
        }

        // let  -> it
        loginData.let { login ->
            login.data.id = "002"
            login.data.nickname = "nickname"
        }

        val let2: String = loginData.let { login ->
            login.data.id = "002"
            login.data.nickname = "nickname"
            "let2"
        }
        L.e(TAG, "let $let2")

        // run -> this
        loginData.run {
            data.id = "003"
            data.nickname = "nickname"
        }

        val run2: String = loginData.run {
            data.id = "003"
            data.nickname = "nickname"
            "run2"
        }
        L.e(TAG, "run $run2")

        // also -> it
        // 上下文对象 作为 lambda 表达式参数（it）来访问。 返回值是上下文对象本身。
        val alsoBean: LoginBean = loginData.also { login ->
            login.data.id = "004"
            login.data.nickname = "nickname"
        }
        L.e("TAG", Gson().toJson(alsoBean))

        // apply -> this
        // 上下文对象 作为接收者（this）来访问。 返回值 是上下文对象本身。
        val applyBean: LoginBean = loginData.apply {
            data.id = "005"
            data.nickname = "nickname"
        }
        L.e("TAG", Gson().toJson(applyBean))
    }

    /**
     * 高阶函数
     * 如果一个函数接收另一个函数作为参数，或者返回值的类型是另一个函数，那么该函数称为高阶函数。
     */
    private fun funcTest() {
        setListener1(javaClass.simpleName) { str ->
            L.e("TAG", str)
        }

        setListener2(javaClass.simpleName) { str ->
            L.e("TAG", str)
        }

        setListener3(javaClass.simpleName) { str, callback ->
            L.e("TAG", str)
            callback.invoke()
        }
        Singleton.getInstance(application).showToast("showToast")
    }

    private fun setListener1(str: String, callback: (str: String) -> Unit) {
        L.e("TAG", str)
        callback.invoke("setListener")
    }

    private fun setListener2(str: String, callback: ((toast: String) -> Unit) = {}) {
        L.e("TAG", str)
        callback.invoke("toast")
    }

    private fun setListener3(str: String, callback: (str: String, callback: () -> Unit) -> Unit) {
        L.e("TAG", str)
        callback.invoke("setListener") {
            L.e("TAG", "Callback")
        }
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

    /**
     * 协程
     * https://developer.android.google.cn/kotlin/coroutines
     *
     * launch、async：启动一个新协程。async支持并发，可以通过await获取返回值
     * withContext：不启动新协程，在原来的协程中切换线程，需要传入一个CoroutineContext对象
     */
    private fun lifecycleScope() {
        //当 activity 处于create的时候执行协程体
        lifecycleScope.launchWhenCreated {
            L.e(TAG, "launchWhenCreated")
        }

        //当 activity 处于start的时候执行协程体
        lifecycleScope.launchWhenStarted {
            L.e(TAG, "launchWhenStarted")
        }

        //当 activity 处于resume的时候执行协程体
        lifecycleScope.launchWhenResumed {
            L.e(TAG, "launchWhenResumed")
        }
    }

    /**
     * 1.启动一个新的线程，在新线程上创建运行协程，不堵塞当前线程
     */
    @DelicateCoroutinesApi
    private fun launch() {
        ThreadUtils.isMainThread("启动协程")
        val job = GlobalScope.launch {
            delay()
        }
        ThreadUtils.isMainThread("主线程执行结束")
        if (job.isCompleted) {
            ThreadUtils.isMainThread("协程执行完成")
            job.cancel()
        }
    }

    /**
     * 2.启动一个新的线程，在新线程上创建运行协程，不堵塞当前线程。支持通过await获取返回值
     */
    @DelicateCoroutinesApi
    private fun await() {
        GlobalScope.launch {
            val async1 = async(3000, 1)
            val async2 = async(5000, 2)
            val result = async1.await() + async2.await()
            L.e("TAG", "result = $result")
        }
    }

    @DelicateCoroutinesApi
    private fun async(timeMillis: Long, result: Int): Deferred<Int> {
        ThreadUtils.isMainThread("启动协程")
        val async = GlobalScope.async {
            delayResult(timeMillis, result)
        }
        ThreadUtils.isMainThread("主线程执行结束")
        return async
    }

    /**
     * 3.创建新的协程，运行在当前线程上，所以会堵塞当前线程，直到协程体结束
     */
    private fun blocking() {
        ThreadUtils.isMainThread("启动阻塞")
        runBlocking {
            delay()
        }
        ThreadUtils.isMainThread("主线程执行结束")
    }

    private suspend fun delay() {
        delay(3000)
        ThreadUtils.isMainThread("协程执行结束")
    }

    private suspend fun delayResult(timeMillis: Long, result: Int): Int {
        delay(timeMillis)
        ThreadUtils.isMainThread("协程执行结束")
        return result
    }

    /**
     * 委托
     */
    fun delegates() {
        Derived(BaseImpl("Derived")).print()
        DerivedCode().toPrint()

        val example = Example()
        L.e("TAG", example.delegate)

        val observable = ObservableExample()
        observable.delegates = "第一次赋值"
        observable.delegates = "第二次赋值"
    }

    /**
     * 可观察属性委托
     */
    class ObservableExample {
        var delegates: String by Delegates.observable("初始值") { property, old, new ->
            L.e("TAG", property.name + "旧值：$old -> 新值：$new")
        }
    }

    /**
     * 属性委托
     */
    class Example {
        var delegate: String by Delegate()
    }

    class Delegate {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
            return "$thisRef, 这里委托了 ${property.name} 属性"
        }

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
            L.e("TAG", "$thisRef 的 ${property.name} 属性赋值为 $value")
        }
    }

    /**
     * 自己实现的委托
     */
    class DerivedCode {
        private val apiImpl = BaseImpl("DerivedCode")
        fun toPrint() {
            apiImpl.print()
        }
    }

    /**
     * 类委托
     */
    class Derived(base: Base) : Base by base

    class BaseImpl(private val str: String) : Base {

        override fun print() {
            L.e("TAG", str)
        }
    }

    interface Base {
        fun print()
    }

    /**
     * reified函数,private java 无法调用
     */
    private fun <T : Activity> Activity.startAct1(context: Context, clazz: Class<T>) {
        startActivity(Intent(context, clazz))
    }

    private inline fun <reified T : Activity> Activity.startAct2(context: Context) {
        startActivity(Intent(context, T::class.java))
    }

    fun reifiedTest() {
        startAct1(this, KotlinActivity::class.java)
        startAct2<KotlinActivity>(this)
    }

}