package com.example.es.data.model.cacheModel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_table")
data class DataCache(
    @PrimaryKey(autoGenerate = true)
    val id_cache: Int,
    val id: Int,
    val full_name: String,
    val phone_user: String,
    val phone_operator: String,
    val photo: String,
    val time_location: String,
    val latitude: String,
    val longitude: String,
    val alarm: Boolean,
    val notify: Boolean
)

