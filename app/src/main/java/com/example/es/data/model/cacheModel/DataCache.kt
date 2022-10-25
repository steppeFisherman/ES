package com.example.es.data.model.cacheModel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_table")
data class DataCache(
    @PrimaryKey(autoGenerate = true)
    val id_cache: Int,
    val id: Int,
    val phone: String,
    val full_name: String,
    val time: String,
    val latitude: String,
    val longitude: String
)

