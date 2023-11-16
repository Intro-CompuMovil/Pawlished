package com.example.pawlished

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ViewStateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_state)

        // Obtén los servicios seleccionados y la peluquería seleccionada
        val serviciosSeleccionados = intent.getStringArrayListExtra("servicios_seleccionados")
        val selectedPeluqueria = intent.getStringExtra("selected-peluqueria")

        // Convierte la lista de servicios seleccionados a mutable
        val serviciosMutable = serviciosSeleccionados?.toMutableList() ?: mutableListOf()

        // Configura la lista de servicios seleccionados
        val serviciosListView: ListView = findViewById(R.id.serviciosRealizadosListView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_checked, serviciosMutable)
        serviciosListView.adapter = adapter

        serviciosListView.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        // Muestra la peluquería seleccionada
        val peluqueriaLabel = findViewById<TextView>(R.id.peluqueriaLabel)
        peluqueriaLabel.text = "$selectedPeluqueria"

        val volverMainButton: Button = findViewById(R.id.volverMainButton)
        volverMainButton.setOnClickListener {
            val intent = Intent(this, MainActivityCliente::class.java)
            startActivity(intent)
            finish()
        }
    }
}
