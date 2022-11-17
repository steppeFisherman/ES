package com.example.es.ui.model

import com.example.es.domain.model.DataDomain

interface MapDomainToUi {

    fun mapDomainToUi(dataDomain: DataDomain): DataUi

    class Base : MapDomainToUi {
        override fun mapDomainToUi(dataDomain: DataDomain): DataUi =
            DataUi(
                id = dataDomain.id,
                id_cache = dataDomain.id_cache,
                full_name = dataDomain.full_name,
                phone_user = dataDomain.phone_user,
                phone_operator = dataDomain.phone_operator,
                photo = dataDomain.photo,
                time_location = dataDomain.time_location,
                latitude = dataDomain.latitude,
                longitude = dataDomain.longitude,
                locationAddress = dataDomain.locationAddress,
                homeAddress = dataDomain.homeAddress,
                alarm = dataDomain.alarm,
                notify = dataDomain.notify
            )
    }
}