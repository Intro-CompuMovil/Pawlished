package com.example.pawlished

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class VerSolicitudesActivity : AppCompatActivity() {

    private lateinit var volverMainButton: Button
    private lateinit var verAceptadasButton: Button
    private lateinit var listView: ListView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var solicitudAdapter: SolicitudAdapter

    private val solicitudesSeleccionadas = mutableListOf<Solicitud>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_solicitudes)

        volverMainButton = findViewById(R.id.volverMainButton)
        verAceptadasButton = findViewById(R.id.verAceptadasButton)
        listView = findViewById(R.id.serviciosSolicitadosListView)
        databaseReference = FirebaseDatabase.getInstance().reference.child("Solicitudes")

        solicitudAdapter = SolicitudAdapter(this, R.layout.item_solicitud, solicitudesSeleccionadas)

        listView.adapter = solicitudAdapter
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        cargarSolicitudes()

        volverMainButton.setOnClickListener {
            val intent = Intent(this, MainActivityPeluqueria::class.java)
            startActivity(intent)
            finish()
        }

        verAceptadasButton.setOnClickListener {
            // Código existente para manejar el evento click del botón verAceptadasButton
            val intent = Intent(this, VerSolicitudesAceptadasActivity::class.java)
            intent.putExtra("solicitudes_seleccionadas", ArrayList(solicitudesSeleccionadas))
            startActivity(intent)
            finish()

        }
    }

    private fun cargarSolicitudes() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val solicitudes = mutableListOf<Solicitud>()
                for (childSnapshot in snapshot.children) {
                    val estado = childSnapshot.child("estado").getValue(String::class.java)
                    val direccion = childSnapshot.child("direccion").getValue(String::class.java)
                    val descripcion = childSnapshot.child("description").getValue(String::class.java)
                    val numero = childSnapshot.child("userPhoneNumber").getValue(String::class.java)
                    val precio = childSnapshot.child("precio").getValue(Double::class.java) ?: 0.0
                    val id = childSnapshot.key

                    if (estado == "Disponible" && !direccion.isNullOrBlank()) {
                        val servicios = mutableListOf<String>()
                        for (i in 0..3) {
                            val servicio = childSnapshot.child("servicio$i").getValue(String::class.java)
                            if (!servicio.isNullOrBlank()) {
                                servicios.add(servicio)
                            }
                        }

                        solicitudes.add(Solicitud(id ?: "", direccion ?: "", descripcion ?: "", servicios, numero ?: "", precio ?: 0.0))
                    }
                }

                // Ordenar las solicitudes por precio de menor a mayor
                val solicitudesOrdenadas = solicitudes.sortedBy { it.precio }

                solicitudAdapter.clear()
                solicitudAdapter.addAll(solicitudesOrdenadas)
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar errores si es necesario
            }
        })
    }
}