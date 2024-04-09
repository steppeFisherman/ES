package com.example.es.data.model.cacheModel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_table")
data class DataCache(
    @PrimaryKey(autoGenerate = true)
    val idCache: Int,
    val id: String,
    val fullName: String,
    val phoneUser: String,
    val phoneOperator: String,
    val photo: String,
    val time: String,
    val timeLong: Long,
    val latitude: Double,
    val longitude: Double,
    val locationAddress: String,
    val homeAddress: String,
    val company: String,
    val alarm: Boolean,
    val notify: Boolean,
    val locationFlagOnly: Boolean,
    val comment: String,
)
