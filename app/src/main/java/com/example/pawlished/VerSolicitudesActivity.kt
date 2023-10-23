package com.example.pawlished

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
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

        solicitudAdapter = SolicitudAdapter(this, R.layout.item_solicitud,solicitudesSeleccionadas)


        listView.adapter = solicitudAdapter
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        cargarSolicitudes()

        volverMainButton.setOnClickListener {
            val intent = Intent(this, MainActivityPeluqueria::class.java)
            startActivity(intent)
            finish()
        }

        verAceptadasButton.setOnClickListener {
            // Obtén los elementos seleccionados
            solicitudesSeleccionadas.clear()
            for (i in 0 until listView.count) {
                if (listView.isItemChecked(i)) {
                    solicitudesSeleccionadas.add(solicitudAdapter.getItem(i) ?: Solicitud("","", "", emptyList(),""))
                }
            }

            // Pasa los elementos seleccionados a VerSolicitudesAceptadasActivity
            val intent = Intent(this, VerSolicitudesAceptadasActivity::class.java)
            val solicitudesSeleccionadasStrings = solicitudesSeleccionadas.map { it.direccion }
            intent.putStringArrayListExtra("solicitudes_seleccionadas", ArrayList(solicitudesSeleccionadasStrings))
            startActivity(intent)
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
                    val id = childSnapshot.key

                    if (estado == "Disponible" && !direccion.isNullOrBlank()) {
                        val servicios = mutableListOf<String>()
                        for (i in 0..3) {
                            val servicio = childSnapshot.child("servicio$i").getValue(String::class.java)
                            if (!servicio.isNullOrBlank()) {
                                servicios.add(servicio)
                            }
                        }

                        solicitudes.add(Solicitud(id?: "",direccion ?: "", descripcion ?: "", servicios,numero?:""))
                    }
                }
                solicitudAdapter.clear()
                solicitudAdapter.addAll(solicitudes)
            }

            override fun onCancelled(error: DatabaseError) {
                // Maneja errores si es necesario
            }
        })
    }
}

