package com.example.es.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ArrayRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.example.es.R

interface ResourceProvider {

    fun getString(context: Context, @StringRes id: Int): String
    fun getStringArray(context: Context, @ArrayRes id: Int): Array<String>
    fun color(context: Context, @ColorRes id: Int): Int
    fun getDrawable(context: Context, @DrawableRes id: Int): Drawable?

    class Base : ResourceProvider {
        override fun getString(context: Context, id: Int) = context.getString(id)
        override fun getStringArray(context: Context, id: Int): Array<String> =
            context.resources.getStringArray(id)

        override fun color(context: Context, @ColorRes id: Int): Int =
            ContextCompat.getColor(context, id)

        override fun getDrawable(context: Context, id: Int): Drawable? =
            ContextCompat.getDrawable(context, id)
    }
}