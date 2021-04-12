package com.example.william.my.module.kotlin.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.william.my.module.kotlin.R
import com.example.william.my.module.kotlin.databinding.KtItemRecycleBinding

class LoadStateViewHolder(parent: ViewGroup, retry: () -> Unit) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.kt_item_recycle, parent, false)
    ) {

    private val context = parent.context
    private val binding = KtItemRecycleBinding.bind(itemView)

    private val mTextView: TextView = binding.itemRecycleTextView.also {
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
                        R.color.colorPrimaryLight
                    )
                )
            }
        }
    }
}

// Adapter that displays a loading spinner when
// state = LoadState.Loading, and an error message and retry
// button when state is LoadState.Error.
class ExampleLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) =
        LoadStateViewHolder(parent, retry)

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) =
        holder.bind(loadState)
}