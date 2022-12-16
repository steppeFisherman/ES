package com.example.es.ui.model

import com.example.es.domain.model.DataDomain
import java.text.DateFormat

interface MapDomainToUi {

    fun mapDomainToUi(dataDomain: DataDomain): DataUi

    class Base : MapDomainToUi {
        override fun mapDomainToUi(dataDomain: DataDomain): DataUi =
            DataUi(
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

        private fun longToDate(timeLong: Long) =
            DateFormat.getDateTimeInstance().format(timeLong)
    }
}