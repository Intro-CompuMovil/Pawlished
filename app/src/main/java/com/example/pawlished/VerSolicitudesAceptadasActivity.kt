package com.example.pawlished

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class VerSolicitudesAceptadasActivity : AppCompatActivity() {

    private lateinit var serviciosAceptadosListView: ListView
    private lateinit var guardarPreciosButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_solicitudes_aceptadas)

        serviciosAceptadosListView = findViewById(R.id.serviciosAceptadosListView)
        guardarPreciosButton = findViewById(R.id.guardarPreciosButton)

        val serviciosSeleccionados = intent.getStringArrayListExtra("servicios_seleccionados") ?: ArrayList()

        // Crear un adaptador personalizado para mostrar los servicios con precios editables
        val adapter = ServiciosAceptadosAdapter(this, serviciosSeleccionados)
        serviciosAceptadosListView.adapter = adapter

        guardarPreciosButton.setOnClickListener {
            // Guardar los precios propuestos aquí
            val preciosPropuestos = adapter.getPreciosPropuestos()
            // se pueden procesar los precios propuestos aquí
        }
    }
}
