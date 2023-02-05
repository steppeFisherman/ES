package com.example.es.utils

import java.text.SimpleDateFormat
import java.util.*

interface DateTimeFormat {

    fun longToStringDateFormat(time: Long): String

    class Base : DateTimeFormat {
        override fun longToStringDateFormat(time: Long): String {
            val format = SimpleDateFormat("HH:mm, dd MMM  yyyy", Locale.getDefault())
            return format.format(time).toString()
        }
    }
}