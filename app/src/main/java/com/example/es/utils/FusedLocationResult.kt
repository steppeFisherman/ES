package com.example.es.utils

import android.annotation.SuppressLint
import android.location.Geocoder
import com.google.android.gms.location.FusedLocationProviderClient
import java.text.DateFormat
import java.util.*

interface FusedLocationResult {

    fun result(
        client: FusedLocationProviderClient,
        geocoder: Geocoder,
        result: (MutableMap<String, Any>) -> Unit
    )

    class Base : FusedLocationResult {

        @SuppressLint("MissingPermission")
        override fun result(
            client: FusedLocationProviderClient,
            geocoder: Geocoder,
            result: (MutableMap<String, Any>) -> Unit
        ) {
            client.lastLocation.addOnSuccessListener { location ->
                if (location != null) {

                    val locationAddress = geocoder
                        .getFromLocation(location.latitude, location.longitude, 2)

                    if (locationAddress.isNotEmpty() && locationAddress.size > 1) {
                        val address = locationAddress[1].getAddressLine(0)
                        val dateDate = Calendar.getInstance(Locale.getDefault()).time
                        val dateString = DateFormat.getDateTimeInstance().format(dateDate)
                        val map = mutableMapOf<String, Any>()
                        map[CHILD_TIME] = dateString
                        map[CHILD_LATITUDE] = location.latitude.toString()
                        map[CHILD_LONGITUDE] = location.longitude.toString()
                        map[CHILD_LOCATION_ADDRESS] = address
                        result(map)
                    }
                }
            }
        }
    }
}