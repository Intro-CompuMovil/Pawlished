package com.example.pawlished

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.Button

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
    }
}
