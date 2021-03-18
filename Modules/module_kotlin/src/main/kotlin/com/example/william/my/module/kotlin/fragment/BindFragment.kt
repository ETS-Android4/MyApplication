package com.example.william.my.module.kotlin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.william.my.module.kotlin.bind.method.CreateMethod
import com.example.william.my.module.kotlin.bind.viewbinding.viewBinding
import com.example.william.my.module.kotlin.databinding.KotlinActivityKotlinBinding

class BindFragment : Fragment() {

    private var binding: KotlinActivityKotlinBinding? = null

    private val viewBinding1: KotlinActivityKotlinBinding by viewBinding()

    private val viewBinding2: KotlinActivityKotlinBinding by viewBinding(CreateMethod.INFLATE)

    private val viewBinding3: KotlinActivityKotlinBinding by viewBinding(KotlinActivityKotlinBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = KotlinActivityKotlinBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}