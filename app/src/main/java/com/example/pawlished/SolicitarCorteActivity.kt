package com.example.pawlished

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

import java.util.*

class SolicitarCorteActivity : AppCompatActivity() {

    private val MY_PERMISSIONS_REQUEST_LOCATION = 99
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var raza : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solicitar_corte)
        raza = findViewById(R.id.razaEditText)
        val databaseReference = FirebaseDatabase.getInstance().reference

        val volverMainButton: Button = findViewById(R.id.volverMainButton)
        val solicitarButton: Button = findViewById(R.id.solicitarButton)
        val serviciosCheckboxList = listOf(
            findViewById<CheckBox>(R.id.bañoCheckBox),
            findViewById<CheckBox>(R.id.corteCheckBox),
            findViewById<CheckBox>(R.id.deslanadoCheckBox),
            findViewById<CheckBox>(R.id.BañoEspecialCheckBox),
            findViewById<CheckBox>(R.id.uñasCheckBox),
            findViewById<CheckBox>(R.id.cuidadoOidosChexkBox),
            findViewById<CheckBox>(R.id.accesoriosCheckBox),
            findViewById<CheckBox>(R.id.fraganciasCheckBox)
        )

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        solicitarButton.setOnClickListener {
            val serviciosSeleccionados = obtenerServiciosSeleccionados(serviciosCheckboxList)


            if (serviciosSeleccionados.isNotEmpty()) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    obtenerUbicacionActual(serviciosSeleccionados)
                } else {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_LOCATION)
                }
            } else {
                Toast.makeText(this, "No has seleccionado ningún servicio", Toast.LENGTH_SHORT).show()
            }
        }

        volverMainButton.setOnClickListener {
            val intent = Intent(this, MainActivityCliente::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun obtenerUbicacionActual(serviciosSeleccionados: List<String>) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Si no tiene permisos, solicita permisos
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_LOCATION)
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val razaMascota = raza.text.toString()
                val latitude = location.latitude
                val longitude = location.longitude

                val direccion = "$latitude, $longitude" // Almacena latitud y longitud en la dirección

                val estadoServicio = "Disponible"

                val currentUser = FirebaseAuth.getInstance().currentUser
                val userId = currentUser?.uid

                val firestore = FirebaseFirestore.getInstance()
                val usersCollection = firestore.collection("clientes")
                val userDocument = usersCollection.document(userId!!)

                userDocument.get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            val userPhoneNumber = documentSnapshot.getString("numeroTelefono")

                            val solicitudReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Solicitudes")
                            val solicitudKey = solicitudReference.push().key
                            val solicitudData = HashMap<String, Any>()

                            solicitudData["direccion"] = direccion
                            solicitudData["estado"] = estadoServicio
                            solicitudData["userId"] = userId
                            solicitudData["description"] = razaMascota

                            // Maneja el valor opcional userPhoneNumber
                            userPhoneNumber?.let {
                                solicitudData["userPhoneNumber"] = it
                            }

                            for ((index, servicio) in serviciosSeleccionados.withIndex()) {
                                solicitudData["servicio$index"] = servicio
                            }

                            if (solicitudKey != null) {
                                solicitudReference.child(solicitudKey).setValue(solicitudData)
                            }

                            val intent = Intent(this, MainActivityCliente::class.java)
                            intent.putStringArrayListExtra("servicios_seleccionados", ArrayList(serviciosSeleccionados))
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "No se pudo obtener el número de teléfono", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error al obtener el número de teléfono: $e", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "No se pudo obtener la ubicación actual", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun obtenerServiciosSeleccionados(checkboxList: List<CheckBox>): ArrayList<String> {
        val serviciosSeleccionados = ArrayList<String>()
        for (checkBox in checkboxList) {
            if (checkBox.isChecked) {
                serviciosSeleccionados.add(checkBox.text.toString())
            }
        }
        return serviciosSeleccionados
    }
}
