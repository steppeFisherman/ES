package com.example.es.domain.model

data class DataDomain(
    var id_cache: Int,
    val id: Int,
    var full_name: String,
    var phone_user: String,
    var phone_operator: String,
    var photo: String,
    var time_location: Long,
    var latitude: String,
    var longitude: String,
    var locationAddress: String,
    var homeAddress: String,
    var alarm: Boolean,
    var notify: Boolean
)