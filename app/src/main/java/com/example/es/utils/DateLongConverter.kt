package com.example.es.utils

import androidx.room.TypeConverter
import java.util.*

class DateLongConverter {

    @TypeConverter
    fun dateToLong(date: Date): Long = date.time
}