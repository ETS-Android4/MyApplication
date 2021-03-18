package com.example.william.my.module.kotlin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.william.my.module.kotlin.bind.method.CreateMethod
import com.example.william.my.module.kotlin.bind.viewbinding.viewBinding
import com.example.william.my.module.kotlin.databinding.KotlinContentKotlinBinding

class BindFragment : Fragment() {

    private var binding: KotlinContentKotlinBinding? = null

    private val viewBinding1: KotlinContentKotlinBinding by viewBinding()

    private val viewBinding2: KotlinContentKotlinBinding by viewBinding(CreateMethod.INFLATE)

    private val viewBinding3: KotlinContentKotlinBinding by viewBinding(KotlinContentKotlinBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = KotlinContentKotlinBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.contentTextView?.text =
            context!!.packageManager.getPackageInfo(context!!.packageName, 0).packageName
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}