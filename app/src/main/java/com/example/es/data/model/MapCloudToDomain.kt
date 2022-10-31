package com.example.es.data.model

import com.example.es.data.model.cloudModel.DataCloud
import com.example.es.domain.model.DataDomain

interface MapCloudToDomain {

    fun mapCloudToDomain(dataCloud: DataCloud): DataDomain

    class Base : MapCloudToDomain {
        override fun mapCloudToDomain(dataCloud: DataCloud): DataDomain =
            DataDomain(
                id = dataCloud.id,
                id_cache = 0,
                full_name = dataCloud.full_name,
                phone_user = dataCloud.phone_user,
                phone_operator = dataCloud.phone_operator,
                photo = dataCloud.photo,
                time = dataCloud.time,
                latitude = dataCloud.latitude,
                longitude = dataCloud.longitude,
                alarm = dataCloud.alarm,
                notify = dataCloud.notify,
            )
    }
}


