package com.example.william.my.module.sample.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.william.my.module.sample.R
import com.example.william.my.module.sample.databinding.SampleItemRecyclerBinding

class PagingStateViewHolder(parent: ViewGroup, retry: () -> Unit) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.sample_item_recycler, parent, false)
) {

    private val context = parent.context
    private val binding = SampleItemRecyclerBinding.bind(itemView)

    private val mTextView: TextView = binding.itemTextView.also {
        it.setOnClickListener {
            retry()
        }
    }

    fun bind(loadState: LoadState) {
        when (loadState) {
            is LoadState.Loading ->
                mTextView.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorPrimary
                    )
                )
            is LoadState.NotLoading ->
                mTextView.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorPrimaryDark
                    )
                )
            is LoadState.Error -> {
                mTextView.text = loadState.error.localizedMessage
                mTextView.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorPrimaryDark
                    )
                )
            }
        }
    }
}