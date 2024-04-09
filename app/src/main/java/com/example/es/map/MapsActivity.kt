package com.example.es.map

import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import com.example.es.R
import com.example.es.ui.model.DataUi
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var geoCoder: Geocoder
    private lateinit var user: DataUi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
        Changes statusBar text/icons color, when the statusBar background is white or transparent
        (this works with API level 21 and higher)
         */
        WindowInsetsControllerCompat(window, window.decorView)
            .isAppearanceLightStatusBars = true
        setContentView(R.layout.activity_maps)

        geoCoder = Geocoder(this, Locale.getDefault())
        val bundle = intent.extras?.getParcelable<DataUi>("user")
        if (bundle != null) user = bundle

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)

        val currentLatLong = LatLng(user.latitude, user.longitude)
        placeMarkerOnMap(currentLatLong)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 18f))
    }

    private fun placeMarkerOnMap(currentLatLong: LatLng) {
        val locationAddress = geoCoder
            .getFromLocation(currentLatLong.latitude, currentLatLong.longitude, 2)?: emptyList()

        if (locationAddress.isNotEmpty() && locationAddress.size > 1) {
            val address = locationAddress[1].getAddressLine(0)
            val markerOptions = MarkerOptions().position(currentLatLong)
            markerOptions.title(address)
            mMap.addMarker(markerOptions)
        }
    }

    override fun onMarkerClick(p0: Marker) = false
}

