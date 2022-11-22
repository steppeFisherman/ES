package com.example.es.domain.model

data class DataDomain(
    val idCache: Int,
    val id: String,
    val fullName: String,
    val phoneUser: String,
    val phoneOperator: String,
    val photo: String,
    val time: String,
    val timeLong: Long,
    val latitude: String,
    val longitude: String,
    val locationAddress: String,
    val homeAddress: String,
    val company: String,
    val alarm: Boolean,
    val notify: Boolean
)