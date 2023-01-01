package com.example.es.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ArrayRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

interface ResourceProvider {

    fun fetchString(@StringRes id: Int): String
    fun fetchStringArray(@ArrayRes id: Int): Array<String>
    fun fetchColor(@ColorRes id: Int): Int
    fun fetchDrawable(@DrawableRes id: Int): Drawable?

    class Base(val context: Context) : ResourceProvider {
        override fun fetchString(@StringRes id: Int) = context.getString(id)
        override fun fetchStringArray(@ArrayRes id: Int): Array<String> =
            context.resources.getStringArray(id)

        override fun fetchColor(@ColorRes id: Int): Int = ContextCompat.getColor(context, id)
        override fun fetchDrawable(@DrawableRes id: Int): Drawable? =
            ContextCompat.getDrawable(context, id)
    }
}