package com.example.pawlished

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView

class VerPeluqueriasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_peluquerias)

        val peluquerias = arrayOf(
            "Peluquería A\nDirección: Calle 123\nHorario: 9:00 AM - 6:00 PM",
            "Peluquería B\nDirección: Calle 456\nHorario: 10:00 AM - 7:00 PM",
            "Peluquería C\nDirección: Calle 789\nHorario: 8:00 AM - 5:00 PM"
        )

        val listView: ListView = findViewById(R.id.peluqueriasListView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, peluquerias)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            // Acción cuando se selecciona una peluquería
            val peluqueriaSeleccionada = peluquerias[position]
            // Puedes agregar lógica adicional aquí, como abrir una actividad para mostrar más detalles
            // de la peluquería o realizar una acción relacionada con la selección.
        }

        val volverMainButton: Button = findViewById(R.id.volverMainButton)
        volverMainButton.setOnClickListener {
            val intent = Intent(this, MainActivityCliente::class.java)
            startActivity(intent)
            finish()
        }

        val mapa: Button = findViewById(R.id.mapa)
        mapa.setOnClickListener {
            val intent = Intent(this, MapaActivity::class.java)
            startActivity(intent)
        }
    }
}
