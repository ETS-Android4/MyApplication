package com.example.william.my.module.sample.paging

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

// Adapter that displays a loading spinner when
// state = LoadState.Loading, and an error message and retry
// button when state is LoadState.Error.
class PagingStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<PagingStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) =
        PagingStateViewHolder(parent, retry)

    override fun onBindViewHolder(holder: PagingStateViewHolder, loadState: LoadState) =
        holder.bind(loadState)
}