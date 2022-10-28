package com.example.es.utils

import android.content.Context
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes

interface ResourceProvider {

    fun getString(context: Context, @StringRes id: Int): String
    fun getStringArray(context: Context, @ArrayRes id: Int): Array<String>

    class Base : ResourceProvider {
        override fun getString(context: Context, id: Int) = context.getString(id)
        override fun getStringArray(context: Context, id: Int): Array<String> =
            context.resources.getStringArray(id)
    }
}