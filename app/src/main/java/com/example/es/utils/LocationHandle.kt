package com.example.es.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.huawei.hms.location.LocationAvailability
import com.huawei.hms.location.LocationRequest
import com.huawei.hms.location.LocationSettingsRequest
import com.huawei.hms.location.LocationSettingsResponse
import java.util.Calendar
import java.util.Locale

typealias Result = (map: MutableMap<String, Any>) -> Unit

interface LocationHandle {

    fun handle(
        context: Context,
        format: DateTimeFormat,
        geocoder: Geocoder,
        result: Result
    )

    class Google(private val client: FusedLocationProviderClient) : LocationHandle {

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun handle(
            context: Context,
            format: DateTimeFormat,
            geocoder: Geocoder,
            result: Result
        ) {

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            client.lastLocation.addOnSuccessListener { location ->

                val map = mutableMapOf<String, Any>()

                if (location != null && location.accuracy < 50) {

                    val mLocationCallback = object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {

                            val latitude = locationResult.lastLocation?.latitude
                            val longitude = locationResult.lastLocation?.longitude

                            if (latitude != null && longitude != null) {

                                val locationAddress =
                                    geocoder.getFromLocation(latitude, longitude, 2)
                                        ?: emptyList()

                                if (locationAddress.isNotEmpty() && locationAddress.size > 1) {

                                    val address = locationAddress[1].getAddressLine(0)
                                    val dateDate =
                                        Calendar.getInstance(Locale.getDefault()).time.time

                                    map[CHILD_TIME] = format.longToStringDateFormat(dateDate)
                                    map[CHILD_TIME_LONG] = dateDate
                                    map[CHILD_LATITUDE] = latitude
                                    map[CHILD_LONGITUDE] = longitude
                                    map[CHILD_LOCATION_ADDRESS] = address
                                    result.invoke(map)
                                    client.removeLocationUpdates(this)
                                }
                            }
                        }
                    }

                    val locationRequest = com.google.android.gms.location.LocationRequest
                        .Builder(2000L).setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                        .build()

                    client.requestLocationUpdates(
                        locationRequest,
                        mLocationCallback,
                        null
                    )
                } else {

                    val mLocationCallback = object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {

                            val latitude = locationResult.lastLocation?.latitude
                            val longitude = locationResult.lastLocation?.longitude

                            if (latitude != null && longitude != null) {

                                val locationAddress =
                                    geocoder.getFromLocation(latitude, longitude, 2)
                                        ?: emptyList()

                                if (locationAddress.isNotEmpty() && locationAddress.size > 1) {

                                    val address = locationAddress[1].getAddressLine(0)
                                    val dateDate =
                                        Calendar.getInstance(Locale.getDefault()).time.time

                                    map[CHILD_TIME] = format.longToStringDateFormat(dateDate)
                                    map[CHILD_TIME_LONG] = dateDate
                                    map[CHILD_LATITUDE] = latitude
                                    map[CHILD_LONGITUDE] = longitude
                                    map[CHILD_LOCATION_ADDRESS] = address
                                    result.invoke(map)
                                    client.removeLocationUpdates(this)
                                }
                            }
                        }
                    }

                    val locationRequest = com.google.android.gms.location.LocationRequest
                        .Builder(2000L).setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                        .build()

                    client.requestLocationUpdates(
                        locationRequest,
                        mLocationCallback,
                        null
                    )
                }
            }
        }
    }

    class Huawei(
        private val client: com.huawei.hms.location.FusedLocationProviderClient,
        private val huaweiSettingsClient: com.huawei.hms.location.SettingsClient,
    ) : LocationHandle {

        private var huaweiLocationCallback: com.huawei.hms.location.LocationCallback? = null
        private var huaweiLocationRequest: com.huawei.hms.location.LocationRequest? = null

        override fun handle(
            context: Context,
            format: DateTimeFormat,
            geocoder: Geocoder,
            result: Result
        ) {

            val map = mutableMapOf<String, Any>()

            huaweiLocationRequest = com.huawei.hms.location.LocationRequest().apply {
//                    interval = 10000
                needAddress = true
                numUpdates = 1
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                Log.d("HHH", "  huaweiApi:")
            }

            if (null == huaweiLocationCallback) {

                huaweiLocationCallback = object : com.huawei.hms.location.LocationCallback() {
                    override fun onLocationResult(huaweiResult: com.huawei.hms.location.LocationResult?) {
                        huaweiResult?.let {
                            val locations: List<Location> = huaweiResult.locations
                            if (locations.isNotEmpty()) {
                                for (location in locations) {
                                    val locationAddress = geocoder
                                        .getFromLocation(
                                            location.latitude,
                                            location.longitude,
                                            2
                                        ) ?: emptyList()

                                    if (locationAddress.isNotEmpty()) {
                                        val address = locationAddress[0].getAddressLine(0)
                                        val dateDate =
                                            Calendar.getInstance(Locale.getDefault()).time.time

                                        map[CHILD_TIME] =
                                            format.longToStringDateFormat(dateDate)
                                        map[CHILD_TIME_LONG] = dateDate
                                        map[CHILD_LATITUDE] = location.latitude.toString()
                                        map[CHILD_LONGITUDE] = location.longitude.toString()
                                        map[CHILD_LOCATION_ADDRESS] = address
                                        Log.i("HHH", "address:$address")

                                    }
                                    Log.i(
                                        "HHH",
                                        "onLocationResult location[Longitude,Latitude,Accuracy]:${location.longitude} , ${location.latitude} , ${location.accuracy}"
                                    )
                                }
                            }
                        }
                    }

                    override fun onLocationAvailability(locationAvailability: LocationAvailability?) {
                        locationAvailability?.let {
                            val flag: Boolean = locationAvailability.isLocationAvailable
                            Log.i("HHH", "onLocationAvailability isLocationAvailable:$flag")
                        }
                    }
                }
            }

            try {
                val builder = LocationSettingsRequest.Builder()
                builder.addLocationRequest(huaweiLocationRequest)
                val locationSettingsRequest = builder.build()
                // Check the device settings before requesting location updates.
                val locationSettingsResponseTask =
                    huaweiSettingsClient.checkLocationSettings(locationSettingsRequest)

                locationSettingsResponseTask.addOnSuccessListener { locationSettingsResponse: LocationSettingsResponse? ->
                    Log.i("HHH", "check location settings success  {$locationSettingsResponse}")
                    // Request location updates.
                    client.requestLocationUpdates(
                        huaweiLocationRequest,
                        huaweiLocationCallback,
                        Looper.getMainLooper()
                    )
                        .addOnSuccessListener {
                            Log.i("HHH", "requestLocationUpdatesWithCallback onSuccess:")
                        }
                        .addOnFailureListener { e ->
                            Log.e(
                                "HHH",
                                "requestLocationUpdatesWithCallback onFailure:${e.message}"
                            )
                        }
                }
                    .addOnFailureListener { e: Exception ->
                        Log.e("HHH", "checkLocationSetting onFailure:${e.message}")
                    }
            } catch (e: Exception) {
                Log.e("HHH", "requestLocationUpdatesWithCallback exception:${e.message}")
            }
        }
    }
}