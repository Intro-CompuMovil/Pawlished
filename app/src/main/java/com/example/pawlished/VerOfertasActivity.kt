package com.example.pawlished

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_ofertas)

        volverMainButton = findViewById(R.id.volverMainButton)
        ofertasListView = findViewById(R.id.ofertasListView)

        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        // Inicializa el adaptador para mostrar las ofertas
        ofertasAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        ofertasListView.adapter = ofertasAdapter

        cargarOfertas()

        volverMainButton.setOnClickListener {
            // Regresar a la actividad principal
            finish()
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

                    // Realiza la consulta para obtener el `userId` de la solicitud
                    val solicitudQuery = solicitudesReference.orderByKey().equalTo(solicitudId)
                    solicitudQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(solicitudSnapshot: DataSnapshot) {
                            if (solicitudSnapshot.childrenCount > 0) {
                                val userIdSolicitud =
                                    solicitudId?.let { solicitudSnapshot.child(it).child("userId").getValue(String::class.java) }

                                // Comprueba si el `userId` coincide con el del usuario actual
                                if (userIdSolicitud == userId) {
                                    // Agrega la oferta a la lista de ofertas
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
}
