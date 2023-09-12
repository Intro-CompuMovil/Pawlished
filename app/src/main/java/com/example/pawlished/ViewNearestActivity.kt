package com.example.pawlished

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class ViewNearestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_nearest)
        val volverMainButton: Button = findViewById(R.id.volverMainButton)
        val peluquerias = arrayOf(
            "el mundo de la mascota\nDirección: Calle Falsa 123\nHorario: 9:00 AM - 6:00 PM",
            "tu perro feliz\nDirección: Avenida Imaginaria 456\nHorario: 10:00 AM - 7:00 PM",
            "el gato felix \nDirección: Plaza Inexistente 789\nHorario: 8:00 AM - 5:00 PM"
        )

        val listView: ListView = findViewById(R.id.peluqueriasListView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, peluquerias)
        listView.adapter = adapter

        volverMainButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            // Obtén la peluquería seleccionada
            val selectedPeluqueria = peluquerias[position]

            // Obtén los servicios seleccionados que pasaste desde SolicitarCorteActivity
            val serviciosSeleccionados = intent.getStringArrayListExtra("servicios_seleccionados")

            // Iniciar ViewStateActivity y pasar los datos
            val intent = Intent(this, ViewStateActivity::class.java)
            intent.putStringArrayListExtra("servicios_seleccionados", serviciosSeleccionados)
            intent.putExtra("selected_peluqueria", selectedPeluqueria)
            startActivity(intent)
        }
    }
}
