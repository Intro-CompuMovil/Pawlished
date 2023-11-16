package com.example.pawlished

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_ver_ofertas, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sort_asc -> {
                sortOffersByPrice(true)
                return true
            }
            R.id.sort_desc -> {
                sortOffersByPrice(false)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
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
                                val userIdSolicitud = solicitudId?.let { solicitudSnapshot.child(it).child("userId").getValue(String::class.java) }

                                if (userIdSolicitud == userId && solicitudId != null && precio != null && peluqueria != null && estado != null) {
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

    private fun sortOffersByPrice(ascending: Boolean) {
        val offersList = ArrayList<String>()
        for (i in 0 until ofertasAdapter.count) {
            offersList.add(ofertasAdapter.getItem(i) ?: "")
        }

        val sortedOffers = offersList.sortedBy {
            it.substringAfter("Precio:").trim().toIntOrNull() ?: 0
        }

        val orderedOffers = if (ascending) sortedOffers else sortedOffers.reversed()
        ofertasAdapter.clear()
        ofertasAdapter.addAll(orderedOffers)
    }
}
