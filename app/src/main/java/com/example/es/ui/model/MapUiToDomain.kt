package com.example.es.ui.model

import com.example.es.domain.model.DataDomain

interface MapUiToDomain {

    fun mapUiToDomain(dataUi: DataUi): DataDomain

    class Base : MapUiToDomain {
        override fun mapUiToDomain(dataUi: DataUi): DataDomain =
            DataDomain(
                id = dataUi.id,
                id_cache = dataUi.id_cache,
                full_name = dataUi.full_name,
                phone_user = dataUi.phone_user,
                phone_operator = dataUi.phone_operator,
                photo = dataUi.photo,
                time_location = dataUi.time_location,
                latitude = dataUi.latitude,
                longitude = dataUi.longitude,
                alarm = dataUi.alarm,
                notify = dataUi.notify
            )
    }
}