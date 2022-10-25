package com.example.es.ui.model

import com.example.es.domain.model.DataDomain

interface MapDomainToUi {

    fun mapDomainToUi(dataDomain: DataDomain): DataUi

    class Base : MapDomainToUi {
        override fun mapDomainToUi(dataDomain: DataDomain): DataUi =
            DataUi(
                id_cache = dataDomain.id_cache,
                id = dataDomain.id,
                phone = dataDomain.phone,
                full_name = dataDomain.full_name,
                time = dataDomain.time,
                latitude = dataDomain.latitude,
                longitude = dataDomain.longitude,
            )
    }
}