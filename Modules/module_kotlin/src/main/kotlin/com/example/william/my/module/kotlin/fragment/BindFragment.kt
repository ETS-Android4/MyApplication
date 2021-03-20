package com.example.william.my.module.kotlin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.william.my.module.kotlin.databinding.KLayoutResponseBinding

class BindFragment : Fragment() {

    private var binding: KLayoutResponseBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = KLayoutResponseBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.contentTextView.text =
            context!!.packageManager.getPackageInfo(context!!.packageName, 0).packageName
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}