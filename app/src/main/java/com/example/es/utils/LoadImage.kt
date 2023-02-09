package com.example.es.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.es.R

interface LoadImage {

    fun load(context: Context, imageView: ImageView, url: String)

    class Base : LoadImage {
        override fun load(context: Context,imageView: ImageView, url: String) {
            Glide.with(imageView.context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(R.drawable.inset_holder_camera)
                .error(R.drawable.inset_holder_camera)
                .into(imageView)
        }
    }
}