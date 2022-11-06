package com.example.es.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.es.R

interface LoadImage {

    fun load(imageView: ImageView, url: String)

    class Base : LoadImage {
        override fun load(imageView: ImageView, url: String) {
            Glide.with(imageView.context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .error(R.drawable.holder)
                .into(imageView)
        }
    }
}