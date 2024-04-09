package com.example.es.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority

interface RequestLocationUpdate {

    fun update(context: Context, client: FusedLocationProviderClient)

    class GoogleLocationUpdate : RequestLocationUpdate {

        override fun update(context: Context, client: FusedLocationProviderClient) {

          val locationRequest = LocationRequest.Builder(1000L)
              .setPriority(Priority.PRIORITY_HIGH_ACCURACY).build()


            val locationCallback: LocationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.locations.forEach {
                        val latitude = it.latitude
                        latitude

                        val longitude = it.longitude
                        longitude
                    }
                }
            }

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            client.requestLocationUpdates(locationRequest,locationCallback, null)
            client.removeLocationUpdates(locationCallback)
        }
    }
}