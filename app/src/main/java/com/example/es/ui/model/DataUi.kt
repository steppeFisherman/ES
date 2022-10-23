package com.example.es.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataUi(
    val id_cache: Int,
    val phone: String,
    val full_name: String,
    val time: String,
    val latitude: String,
    val longitude: String
): Parcelable