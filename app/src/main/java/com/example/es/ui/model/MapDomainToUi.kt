package com.example.es.ui.model

import com.example.es.domain.model.DataDomain
import java.text.DateFormat
import java.time.format.DateTimeFormatter

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
                time_location = longToDate(dataDomain.time_location),
                latitude = dataDomain.latitude,
                longitude = dataDomain.longitude,
                locationAddress = dataDomain.locationAddress,
                homeAddress = dataDomain.homeAddress,
                alarm = dataDomain.alarm,
                notify = dataDomain.notify
            )

        private fun longToDate(timeLong: Long): String {
//            return DateFormat.getDateInstance().format(timeLong)
           return DateFormat.getDateTimeInstance().format(timeLong)
        }
    }
}