package com.example.es.data.model.cloudModel

data class DataCloud(
    val id: String = "",
    val idCache: Int = 0,
    val fullName: String = "",
    val phoneUser: String = "",
    val phoneOperator: String = "",
    val photo: String = "",
    val time: String = "",
    val timeLong: Long = 0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val locationAddress: String = "",
    val homeAddress: String = "",
    val company: String = "",
    val alarm: Boolean = false,
    val notify: Boolean = false,
    val locationFlagOnly: Boolean = false,
    val comment: String = ""
)
