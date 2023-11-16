package com.example.pawlished

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class VerOfertasActivity : AppCompatActivity() {

    private lateinit var volverMainButton: Button
    private lateinit var ofertasListView: ListView
    private lateinit var ofertasAdapter: ArrayAdapter<String>
    private lateinit var userId: String
    private var serviciosSeleccionados: ArrayList<String>? = null // Variable para almacenar los servicios seleccionados


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_ofertas)

        volverMainButton = findViewById(R.id.volverMainButton)
        ofertasListView = findViewById(R.id.ofertasListView)

        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        serviciosSeleccionados = intent.getStringArrayListExtra("servicios_seleccionados")

        // Inicializa el adaptador para mostrar las ofertas
        ofertasAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        ofertasListView.adapter = ofertasAdapter

        cargarOfertas()

        volverMainButton.setOnClickListener {
            finish()
        }

        ofertasListView.setOnItemClickListener { _, _, position, _ ->
            val selectedOffer = ofertasAdapter.getItem(position)
            startViewStateActivity(selectedOffer)
        }
    }

    private fun cargarOfertas() {
        val databaseReference = FirebaseDatabase.getInstance().reference
        val ofertasReference = databaseReference.child("ofertas")
        val solicitudesReference = databaseReference.child("Solicitudes")

        val ofertasQuery = ofertasReference.orderByChild("solicitudId")

        ofertasQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                ofertasAdapter.clear()
                for (ofertaSnapshot in snapshot.children) {
                    val solicitudId = ofertaSnapshot.child("solicitudId").getValue(String::class.java)
                    val precio = ofertaSnapshot.child("precio").getValue(String::class.java)
                    val peluqueria = ofertaSnapshot.child("Peluqueria").getValue(String::class.java)
                    val estado = ofertaSnapshot.child("Estado").getValue(String::class.java)

                    val solicitudQuery = solicitudesReference.orderByKey().equalTo(solicitudId)
                    solicitudQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(solicitudSnapshot: DataSnapshot) {
                            if (solicitudSnapshot.childrenCount > 0) {
                                val userIdSolicitud =
                                    solicitudId?.let { solicitudSnapshot.child(it).child("userId").getValue(String::class.java) }

                                if (userIdSolicitud == userId) {
                                    val ofertaStr = "Solicitud ID: $solicitudId, Precio: $precio, Peluqueria: $peluqueria, Estado: $estado"
                                    ofertasAdapter.add(ofertaStr)
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Maneja errores si es necesario
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Maneja errores si es necesario
            }
        })
    }

    private fun startViewStateActivity(selectedOffer: String?) {
        val intent = Intent(this, ViewStateActivity::class.java)
        intent.putStringArrayListExtra("servicios_seleccionados", serviciosSeleccionados)

        // Extracción del nombre de la peluquería del texto seleccionado
        val lines = selectedOffer?.split("\n")
        val selectedPeluqueria = lines?.get(1) // Suponiendo que el nombre de la peluquería está en la segunda línea
        intent.putExtra("selected-peluqueria", selectedPeluqueria)

        intent.putExtra("selected_offer", selectedOffer)
        startActivity(intent)
    }
}

