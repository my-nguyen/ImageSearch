package com.florian_walther.imagesearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.florian_walther.imagesearch.databinding.ItemUnsplashPhotoBinding
import com.florian_walther.mvvmimagesearch.Photo

class PhotoAdapter(val listener: OnItemClickListener): PagingDataAdapter<Photo, PhotoAdapter.ViewHolder>(PHOTO_COMPARATOR) {

    companion object {
        val PHOTO_COMPARATOR = object: DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo) =
                oldItem == newItem
        }
    }

    interface OnItemClickListener {
        fun onItemClick(photo: Photo)
    }

    inner class ViewHolder(val binding: ItemUnsplashPhotoBinding): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemClick(item)
                    }
                }
            }
        }

        fun bind(photo: Photo) {
            binding.apply {
                val transition = DrawableTransitionOptions.withCrossFade()
                Glide.with(itemView)
                    .load(photo.urls.regular)
                    .centerCrop()
                    .transition(transition)
                    .error(R.drawable.ic_error)
                    .into(imageView)
                tvUsername.text = photo.user.username
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUnsplashPhotoBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }
}