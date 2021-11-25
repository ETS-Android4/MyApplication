package com.example.william.my.module.sample.activity

import android.os.Bundle
import android.util.Log
import com.example.william.my.library.base.BaseActivity
import com.example.william.my.module.sample.databinding.KtLayoutResponseBinding
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

/**
 * 委托
 */
class DelegatesActivity : BaseActivity() {

    lateinit var binding: KtLayoutResponseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = KtLayoutResponseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.contentTextView.setOnClickListener {

            Derived(BaseImpl("Derived")).print()
            DerivedCode().toPrint()

            val example = Example()
            Log.e("TAG", example.delegate)

            val observable = ObservableExample()
            observable.delegates = "第一次赋值"
            observable.delegates = "第二次赋值"
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
            Log.e("TAG", "$thisRef 的 ${property.name} 属性赋值为 $value")
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

    class BaseImpl(private val str: String) : Base {

        override fun print() {
            Log.e("TAG", str)
        }
    }

    interface Base {
        fun print()
    }
}
