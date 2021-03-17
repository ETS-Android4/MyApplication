package com.example.william.my.module.kotlin.property

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.william.my.module.kotlin.databinding.KotlinActivityKotlinBinding

class BindFragment : Fragment() {

    private var binding: KotlinActivityKotlinBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = KotlinActivityKotlinBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}