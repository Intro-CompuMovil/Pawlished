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

        val volverMainButton: Button = findViewById(R.id.volverMainButton)

        serviciosAceptadosListView = findViewById(R.id.serviciosAceptadosListView)
        guardarPreciosButton = findViewById(R.id.guardarPreciosButton)

        val serviciosSeleccionados = intent.getStringArrayListExtra("servicios_seleccionados") ?: ArrayList()

        // Crear un adaptador para mostrar los elementos seleccionados
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, serviciosSeleccionados)
        serviciosAceptadosListView.adapter = adapter

        volverMainButton.setOnClickListener {
            val intent = Intent(this, MainActivityPeluqueria::class.java)
            startActivity(intent)
            finish()
        }

        guardarPreciosButton.setOnClickListener {
            TODO()
        }
    }
}
