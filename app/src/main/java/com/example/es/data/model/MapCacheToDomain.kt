package com.example.es.data.model

import com.example.es.data.model.cacheModel.DataCache
import com.example.es.domain.model.DataDomain

interface MapCacheToDomain {

    fun mapCacheToDomain(dataCache: DataCache): DataDomain
//    fun listMapCacheToDomain(list: List<DataCache>): List<DataDomain>

    class Base : MapCacheToDomain {

        override fun mapCacheToDomain(dataCache: DataCache): DataDomain =
            DataDomain(
                id = dataCache.id,
                id_cache = dataCache.id_cache,
                full_name = dataCache.full_name,
                phone_user = dataCache.phone_user,
                phone_operator = dataCache.phone_operator,
                photo = dataCache.photo,
                time_location = dataCache.time_location,
                latitude = dataCache.latitude,
                longitude = dataCache.longitude,
                locationAddress = dataCache.locationAddress,
                homeAddress = dataCache.homeAddress,
                alarm = dataCache.alarm,
                notify = dataCache.notify,
            )

        fun listMapCacheToDomain(list: List<DataCache>): List<DataDomain> =
            list.map { dataCache -> mapCacheToDomain(dataCache) }

//
//       private fun listMapCacheToDomain(list: List<DataCache>): List<DataDomain> =
//            list.map { dataCache -> mapCacheToDomain(dataCache) }
    }
}