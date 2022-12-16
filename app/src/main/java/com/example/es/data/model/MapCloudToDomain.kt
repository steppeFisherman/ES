package com.example.es.data.model

import com.example.es.data.model.cloudModel.DataCloud
import com.example.es.domain.model.DataDomain

interface MapCloudToDomain {

    fun mapCloudToDomain(dataCloud: DataCloud): DataDomain

    class Base : MapCloudToDomain {
        override fun mapCloudToDomain(dataCloud: DataCloud): DataDomain =
            DataDomain(
                id = dataCloud.id,
                idCache = dataCloud.idCache,
                fullName = dataCloud.fullName,
                phoneUser = dataCloud.phoneUser,
                phoneOperator = dataCloud.phoneOperator,
                photo = dataCloud.photo,
                time = dataCloud.time,
                timeLong = dataCloud.timeLong,
                latitude = dataCloud.latitude,
                longitude = dataCloud.longitude,
                locationAddress = dataCloud.locationAddress,
                homeAddress = dataCloud.homeAddress,
                company = dataCloud.company,
                alarm = dataCloud.alarm,
                notify = dataCloud.notify,
                locationFlagOnly = dataCloud.locationFlagOnly,
                comment = dataCloud.comment
            )
    }
}


