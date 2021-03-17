package com.florian_walther.imagesearch

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.florian_walther.imagesearch.databinding.FragmentDetailBinding

class DetailFragment: Fragment(R.layout.fragment_detail) {
    val args by navArgs<DetailFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDetailBinding.bind(view)
        binding.apply {
            val photo = args.photo
            // in a Fragment or Activity, use this@DetailFragment and not a View for context,
            // because View is problematic and not efficient
            Glide.with(this@DetailFragment)
                .load(photo.urls.full)
                .error(R.drawable.ic_error)
                .listener(object: RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?,
                                              isFirstResource: Boolean): Boolean {
                        progressBar.isVisible = false
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?,
                                                 dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        progressBar.isVisible = false
                        tvCreator.isVisible = true
                        tvDescription.isVisible = photo.description != null

                        // must return false; otherwise image won't show
                        return false
                    }
                }).into(imageView)

            tvDescription.text = photo.description

            val uri = Uri.parse(photo.user.attributionUrl)
            val intent = Intent(Intent.ACTION_VIEW, uri)

            // set tvCreator field clickable
            tvCreator.apply {
                text = "Photo by ${photo.user.name} on Unsplash"
                setOnClickListener {
                    context.startActivity(intent)
                }
                // show underline
                paint.isUnderlineText = true
            }
        }
    }
}