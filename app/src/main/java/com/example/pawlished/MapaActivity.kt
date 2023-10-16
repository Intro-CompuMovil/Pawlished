package com.example.pawlished

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.pawlished.databinding.ActivityMapaBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.MapStyleOptions

class MapaActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapaBinding
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    var latitud = 0.0
    var longitud = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val seleccionarTienda:Button = findViewById(R.id.seleccionarTienda)



        binding = ActivityMapaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        requestPermission()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)





    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                this@MapaActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@MapaActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {

            }
            ActivityCompat.requestPermissions(
                this@MapaActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        } else if (ActivityCompat.checkSelfPermission(
                this@MapaActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@MapaActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            setLocation()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setLocation()
                }
                return
            }

            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setLocation()
                }
                return
            }

            else -> {
                //algo mas
            }
        }
    }



    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json))
        //setear la localización
        if (ActivityCompat.checkSelfPermission(
                this@MapaActivity, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@MapaActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mFusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                Log.i("LOCATION", "onSucess location")
                if (location != null) {
                    latitud = location.latitude
                    longitud = location.longitude
                    Log.i("miLatitud", latitud.toString())
                    Log.i("miLongitud", longitud.toString())
                    //Poner un marcador en la ubicación del usuario
                    var miUbicacion = LatLng(latitud, longitud)
                    Log.i("Mi ubicación", miUbicacion.toString())
                    mMap.moveCamera(CameraUpdateFactory.zoomTo(15.0f))
                    mMap.addMarker(MarkerOptions().position(miUbicacion).title("Mi ubicación"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(miUbicacion))


                   //Poner marcadores de las tiendas que


                }
            }

        } else {
            requestPermission()
        }





        mMap.uiSettings.isZoomControlsEnabled = true // Habilitar los "gestures" como "pinch to zoom"
        mMap.uiSettings.isZoomGesturesEnabled = true // Habilitar los botones de zoom
    }

    private fun setLocation() {
        //setear la localización
        if (ActivityCompat.checkSelfPermission(
                this@MapaActivity, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@MapaActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mFusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                Log.i("LOCATION", "onSucess location")
                if (location != null) {
                    latitud = location.latitude
                    longitud = location.longitude
                    Log.i("miLatitud", latitud.toString())
                    Log.i("miLongitud", longitud.toString())
                }
            }
        }
    }
}