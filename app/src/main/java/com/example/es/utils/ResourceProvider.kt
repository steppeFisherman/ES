package com.example.es.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ArrayRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

interface ResourceProvider {

    fun getString(@StringRes id: Int): String
    fun getStringArray(@ArrayRes id: Int): Array<String>
    fun color(@ColorRes id: Int): Int
    fun getDrawable(@DrawableRes id: Int): Drawable?

    class Base(val context: Context) : ResourceProvider {
        override fun getString(id: Int) = context.getString(id)
        override fun getStringArray(id: Int): Array<String> = context.resources.getStringArray(id)
        override fun color(@ColorRes id: Int): Int = ContextCompat.getColor(context, id)
        override fun getDrawable(id: Int): Drawable? = ContextCompat.getDrawable(context, id)
    }
}