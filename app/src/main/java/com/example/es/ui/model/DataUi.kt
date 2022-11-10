package com.example.es.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataUi(
    var id_cache: Int,
    val id: Int,
    var full_name: String,
    var phone_user: String,
    var phone_operator: String,
    var photo: String,
    var time_location: String,
    var latitude: String,
    var longitude: String,
    var alarm: Boolean,
    var notify: Boolean
): Parcelable