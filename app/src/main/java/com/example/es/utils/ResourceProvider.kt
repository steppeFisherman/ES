package com.example.es.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.annotation.ArrayRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import com.example.es.R

interface ResourceProvider {

    fun getString(context: Context, @StringRes id: Int): String
    fun getStringArray(context: Context, @ArrayRes id: Int): Array<String>
    fun getColor(context: Context, @ColorRes id: Int): ColorStateList

    class Base : ResourceProvider {
        override fun getString(context: Context, id: Int) = context.getString(id)
        override fun getStringArray(context: Context, id: Int): Array<String> =
            context.resources.getStringArray(id)

        override fun getColor(context: Context, id: Int): ColorStateList =
            AppCompatResources.getColorStateList(context, id)
    }
}