package com.example.es.data.model

import com.example.es.data.model.cloudModel.DataCloud
import com.example.es.domain.model.DataDomain

interface MapCloudToDomain {

    fun mapCloudToDomain(dataCloud: DataCloud): DataDomain

    class Base : MapCloudToDomain {
        override fun mapCloudToDomain(dataCloud: DataCloud): DataDomain =
            DataDomain(
                id = dataCloud.id,
                id_cache = dataCloud.id_cache,
                full_name = dataCloud.full_name,
                phone_user = dataCloud.phone_user,
                phone_operator = dataCloud.phone_operator,
                photo = dataCloud.photo,
                time_location = dataCloud.time_location,
                latitude = dataCloud.latitude,
                longitude = dataCloud.longitude,
                alarm = dataCloud.alarm,
                notify = dataCloud.notify,
            )
    }
}


