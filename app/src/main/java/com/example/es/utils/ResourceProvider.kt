package com.example.es.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.annotation.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.es.R

interface ResourceProvider {

    fun getString(context: Context, @StringRes id: Int): String
    fun getStringArray(context: Context, @ArrayRes id: Int): Array<String>
    fun getColor(context: Context, @ColorRes id: Int): ColorStateList
    fun getDrawable(context: Context, @DrawableRes id: Int): Drawable?

    class Base : ResourceProvider {
        override fun getString(context: Context, id: Int) = context.getString(id)
        override fun getStringArray(context: Context, id: Int): Array<String> =
            context.resources.getStringArray(id)

        override fun getColor(context: Context, id: Int): ColorStateList =
            AppCompatResources.getColorStateList(context, id)

        override fun getDrawable(context: Context, id: Int): Drawable? =
            ContextCompat.getDrawable(context, id)
    }
}