package com.example.es.data.model

import com.example.es.data.model.cacheModel.DataCache
import com.example.es.data.model.cloudModel.DataCloud

interface MapCloudToCache {

    fun mapCloudToCache(dataCloud: DataCloud): DataCache

    class Base : MapCloudToCache {
        override fun mapCloudToCache(dataCloud: DataCloud): DataCache =
            DataCache(
                id = 0,
                phone = dataCloud.phone,
                full_name = dataCloud.full_name,
                time = dataCloud.time,
                latitude = dataCloud.latitude,
                longitude = dataCloud.longitude
            )
    }
}


