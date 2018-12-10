package com.example.biolizard.mapexample2

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.View
import android.widget.AdapterView

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.widget.Spinner
import com.google.android.gms.location.places.ui.PlacePicker
import android.widget.Toast
import com.google.android.gms.location.places.Place
import android.content.Intent
import android.support.design.widget.FloatingActionButton


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var sp_mapType: Spinner
    private lateinit var btn_search: FloatingActionButton
    companion object {
        const val USER_LOCATION_REQUEST_CODE = 100
        const val PLACE_PICKER_REQUEST: Int = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        sp_mapType = findViewById<Spinner>(R.id.spinner_mapType)
        btn_search = findViewById<FloatingActionButton>(R.id.btn_search)

        btn_search.setOnClickListener {
            val builder = PlacePicker.IntentBuilder()
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST)
        }

        sp_mapType?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


                when (position) {
                    0 -> mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                    1 -> mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                    2 -> mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE

                }
            }

        }
        mapFragment.getMapAsync(this)
        requestLocationPermission()
    }
    private fun requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

                requestPermissions(
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    USER_LOCATION_REQUEST_CODE
                )
            }
        }
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    val place = PlacePicker.getPlace(this, data)
                    val toastMsg = String.format("Place: %s", place.name)
                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show()

                    mMap.addMarker(MarkerOptions().position(place.latLng).title(place.name.toString()))
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.latLng, 12f))

                }
            }
        }
    }





    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val cuenca = LatLng(40.070393, -2.137416)
        mMap.addMarker(MarkerOptions().position(cuenca).title("Marker in Cuenca"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cuenca))
        mMap.uiSettings.isZoomControlsEnabled = true

    }

}
