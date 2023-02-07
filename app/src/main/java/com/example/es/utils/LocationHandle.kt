package com.example.es.utils

import android.annotation.SuppressLint
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.huawei.hms.location.LocationAvailability
import com.huawei.hms.location.LocationRequest
import com.huawei.hms.location.LocationSettingsRequest
import com.huawei.hms.location.LocationSettingsResponse
import java.util.*

typealias ListenerApi = (MutableMap<String, Any>) -> Unit

interface LocationHandle {

    fun handle(
        format: DateTimeFormat,
        geocoder: Geocoder,
        result: (MutableMap<String, Any>) -> Unit
    )

    class Google(private val client: FusedLocationProviderClient) : LocationHandle {

        @SuppressLint("MissingPermission")
        override fun handle(
            format: DateTimeFormat,
            geocoder: Geocoder,
            result: (MutableMap<String, Any>) -> Unit
        ) {
            client.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    Log.d("AAA", "  client.lastLocation isNOT null")
                    val locationAddress = geocoder
                        .getFromLocation(location.latitude, location.longitude, 2)

                    if (locationAddress.isNotEmpty() && locationAddress.size > 1) {

                        val address = locationAddress[1].getAddressLine(0)
                        val dateDate = Calendar.getInstance(Locale.getDefault()).time.time

                        val map = mutableMapOf<String, Any>()
                        map[CHILD_TIME] = format.longToStringDateFormat(dateDate)
                        map[CHILD_TIME_LONG] = dateDate
                        map[CHILD_LATITUDE] = location.latitude.toString()
                        map[CHILD_LONGITUDE] = location.longitude.toString()
                        map[CHILD_LOCATION_ADDRESS] = address
                        result(map)
                    }
                } else {
                    Log.d("AAA", "  client.lastLocation is null")
                }
            }
        }
    }

    class Huawei(
        private val client: com.huawei.hms.location.FusedLocationProviderClient,
        private val huaweiSettingsClient: com.huawei.hms.location.SettingsClient,
        private var huaweiLocationCallback: com.huawei.hms.location.LocationCallback?
    ) : LocationHandle {

        private var huaweiLocationRequest: com.huawei.hms.location.LocationRequest? = null

        override fun handle(
            format: DateTimeFormat,
            geocoder: Geocoder,
            result: (MutableMap<String, Any>) -> Unit
        ) {

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
                        if (huaweiResult != null) {
                            val locations: List<Location> = huaweiResult.locations
                            if (locations.isNotEmpty()) {
                                for (location in locations) {
                                    val locationAddress = geocoder
                                        .getFromLocation(
                                            location.latitude,
                                            location.longitude,
                                            2
                                        )
                                    locationAddress.size
                                    if (locationAddress.isNotEmpty() && locationAddress.size > 0) {
                                        val address = locationAddress[0].getAddressLine(0)
                                        val dateDate =
                                            Calendar.getInstance(Locale.getDefault()).time.time

                                        val map = mutableMapOf<String, Any>()
                                        map[CHILD_TIME] =
                                            format.longToStringDateFormat(dateDate)
                                        map[CHILD_TIME_LONG] = dateDate
                                        map[CHILD_LATITUDE] = location.latitude.toString()
                                        map[CHILD_LONGITUDE] = location.longitude.toString()
                                        map[CHILD_LOCATION_ADDRESS] = address
                                        Log.i("HHH", "address:$address")
                                        result(map)
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