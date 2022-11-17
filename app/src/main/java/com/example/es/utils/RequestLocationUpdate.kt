package com.example.es.utils

import android.annotation.SuppressLint
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult

interface RequestLocationUpdate {

    fun update(client: FusedLocationProviderClient)

    class Base : RequestLocationUpdate {
        @SuppressLint("MissingPermission")
        override fun update(client: FusedLocationProviderClient) {
            val locationRequest = LocationRequest.create() // Create location request.
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY // Set priority.
            locationRequest.interval = 2 * 1000
            locationRequest.fastestInterval = 1 * 1000

            val locationCallback: LocationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {}
            }

            client.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }
}