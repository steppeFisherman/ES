package com.example.es.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataUi(
    val id_cache: Int = 0,
    val id: Int = 0,
    val full_name: String = "",
    val phone_user: String = "",
    val phone_operator: String = "",
    val photo: String = "",
    val time: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val alarm: Boolean = false,
    val notify: Boolean = false,
): Parcelable