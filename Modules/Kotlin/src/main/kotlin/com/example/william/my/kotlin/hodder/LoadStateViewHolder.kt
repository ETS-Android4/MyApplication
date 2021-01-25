package com.example.william.my.kotlin.hodder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.william.my.kotlin.R
import com.example.william.my.kotlin.databinding.KotlinItemRecycleBinding

class LoadStateViewHolder(parent: ViewGroup, retry: () -> Unit) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.kotlin_item_recycle, parent, false)
    ) {

    private val binding = KotlinItemRecycleBinding.bind(itemView)

    private val mTextView: TextView = binding.itemRecycleTextView.also {
        it.setOnClickListener {
            retry()
        }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            mTextView.text = loadState.error.localizedMessage
        }
        mTextView.text = "LoadState"
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