package com.example.william.my.module.kotlin

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.module.kotlin.bind.method.CreateMethod
import com.example.william.my.module.kotlin.bind.viewbinding.viewBinding
import com.example.william.my.module.kotlin.databinding.KotlinActivityKotlinBinding
import com.example.william.my.module.router.ARouterPath
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

/**
 * https://developer.android.google.cn/kotlin/ktx
 */
@Route(path = ARouterPath.Kotlin.Kotlin)
class KotlinActivity : AppCompatActivity() {

    private var binding: KotlinActivityKotlinBinding? = null

    private val viewBinding1: KotlinActivityKotlinBinding by viewBinding()

    private val viewBinding2: KotlinActivityKotlinBinding by viewBinding(CreateMethod.INFLATE)

    private val viewBinding3: KotlinActivityKotlinBinding by viewBinding(KotlinActivityKotlinBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = KotlinActivityKotlinBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        //setContentView(R.layout.kotlin_activity_kotlin)

//        binding!!.activityTextView.setOnClickListener {
//
//            Derived(BaseImpl("Derived")).print()
//            DerivedCode().toPrint()
//
//            val example = Example()
//            Log.e("TAG", example.delegate)
//
//            val observable = ObservableExample()
//            observable.delegates = "第一次赋值"
//            observable.delegates = "第二次赋值"
//
//            Singleton.getInstance(application).showToast("Singleton")
//        }
    }
}

//可观察属性委托
class ObservableExample {
    var delegates: String by Delegates.observable("初始值") { property, old, new ->
        Log.e("TAG", property.name + "旧值：$old -> 新值：$new")
    }
}

//属性委托
class Example {
    var delegate: String by Delegate()
}

class Delegate {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return "$thisRef, 这里委托了 ${property.name} 属性"
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        println("$thisRef 的 ${property.name} 属性赋值为 $value")
    }
}

//自己实现的委托
class DerivedCode {
    private val apiImpl = BaseImpl("DerivedCode")
    fun toPrint() {
        apiImpl.print()
    }
}

//类委托
class Derived(base: Base) : Base by base

interface Base {
    fun print()
}

class BaseImpl(private val str: String) : Base {

    override fun print() {
        Log.e("TAG", str)
    }
}