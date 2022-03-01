package com.example.william.my.module.sample.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.william.my.module.sample.R
import com.example.william.my.module.sample.databinding.SampleItemRecyclerBinding

// Adapter that displays a loading spinner when
// state = LoadState.Loading, and an error message and retry
// button when state is LoadState.Error.
class PagingLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) =
        LoadStateViewHolder(parent, retry)

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) =
        holder.bind(loadState)
}

class LoadStateViewHolder(parent: ViewGroup, retry: () -> Unit) : RecyclerView.ViewHolder(
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