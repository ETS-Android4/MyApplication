package com.example.william.my.module.kotlin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.william.my.module.kotlin.R
import com.example.william.my.module.kotlin.databinding.KtLayoutResponseBinding

class BindFragment : Fragment() {

    // This property is only valid between onCreateView and onDestroyView.
    private var _binding: KtLayoutResponseBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = KtLayoutResponseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.contentTextView.text = resources.getString(R.string.kt_app_name)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}