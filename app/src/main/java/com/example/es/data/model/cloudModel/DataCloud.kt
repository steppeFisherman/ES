package com.example.es.data.model.cloudModel

data class DataCloud(
    val id_cache: Int = 0,
    val id: Int = 0,
    var full_name: String = "",
    var phone_user: String = "",
    var phone_operator: String = "",
    var photo: String = "",
    var time_location: Long = 0,
    var latitude: String = "",
    var longitude: String = "",
    val locationAddress: String = "",
    val homeAddress: String = "",
    var alarm: Boolean = false,
    var notify: Boolean = false
)
