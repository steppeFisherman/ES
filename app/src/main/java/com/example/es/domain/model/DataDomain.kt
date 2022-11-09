package com.example.es.domain.model

data class DataDomain(
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
)