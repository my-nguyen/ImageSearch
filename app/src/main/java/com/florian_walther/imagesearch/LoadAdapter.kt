package com.florian_walther.imagesearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.florian_walther.imagesearch.databinding.LoadStateBinding

class LoadAdapter(val retry: () -> Unit): LoadStateAdapter<LoadAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: LoadStateBinding): RecyclerView.ViewHolder(binding.root) {

        init {
            // avoid setOnClickListener in LoadAdapter.onBindViewHolder() because it would be repeated
            // over and over. whereas only a few ViewHolder's will be created by the RecyclerView
            binding.btnRetry.setOnClickListener {
                retry.invoke()
            }
        }
        fun bind(loadState: LoadState) {
            binding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                btnRetry.isVisible = loadState !is LoadState.Loading
                tvError.isVisible = loadState !is LoadState.Loading
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LoadStateBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }
}