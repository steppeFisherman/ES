package com.example.es.data.model

import com.example.es.data.model.cacheModel.DataCache
import com.example.es.domain.model.DataDomain

interface MapCacheToDomain {

    fun mapCacheToDomain(dataCache: DataCache): DataDomain

    class Base : MapCacheToDomain {
        override fun mapCacheToDomain(dataCache: DataCache): DataDomain =
            DataDomain(
                id_cache = dataCache.id,
                phone = dataCache.phone,
                full_name = dataCache.full_name,
                time = dataCache.time,
                latitude = dataCache.latitude,
                longitude = dataCache.longitude,
            )
    }
}