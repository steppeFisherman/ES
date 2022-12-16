package com.example.es.data.model

import com.example.es.data.model.cloudModel.DataCloud
import com.example.es.domain.model.DataDomain

interface MapDomainToCloud {

    fun mapDomainToCloud(dataDomain: DataDomain): DataCloud

    class Base : MapDomainToCloud {
        override fun mapDomainToCloud(dataDomain: DataDomain): DataCloud =
            DataCloud(
                id = dataDomain.id,
                idCache = dataDomain.idCache,
                fullName = dataDomain.fullName,
                phoneUser = dataDomain.phoneUser,
                phoneOperator = dataDomain.phoneOperator,
                photo = dataDomain.photo,
                time = dataDomain.time,
                timeLong = dataDomain.timeLong,
                latitude = dataDomain.latitude,
                longitude = dataDomain.longitude,
                locationAddress = dataDomain.locationAddress,
                homeAddress = dataDomain.homeAddress,
                company = dataDomain.company,
                alarm = dataDomain.alarm,
                notify = dataDomain.notify,
                locationFlagOnly = dataDomain.locationFlagOnly,
                comment = dataDomain.comment
            )
    }
}


