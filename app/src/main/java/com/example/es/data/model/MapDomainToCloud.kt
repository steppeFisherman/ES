package com.example.es.data.model

import com.example.es.data.model.cloudModel.DataCloud
import com.example.es.domain.model.DataDomain

interface MapDomainToCloud {

    fun mapDomainToCloud(dataDomain: DataDomain): DataCloud

    class Base : MapDomainToCloud {
        override fun mapDomainToCloud(dataDomain: DataDomain): DataCloud =
            DataCloud(
                id = dataDomain.id,
                full_name = dataDomain.full_name,
                phone_user = dataDomain.phone_user,
                phone_operator = dataDomain.phone_operator,
                photo = dataDomain.photo,
                time_location = dataDomain.time_location,
                latitude = dataDomain.latitude,
                longitude = dataDomain.longitude,
                alarm = dataDomain.alarm,
                notify = dataDomain.notify,
            )
    }
}


