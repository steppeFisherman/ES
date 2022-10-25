package com.example.es.data.model.cloudModel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataCloud(
    val id: Int= 0,
    val phone: String = "",
    val full_name: String = "",
    val time: String = "",
    val latitude: String = "",
    val longitude: String = ""
    ): Parcelable