package com.example.pawlished

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class ViewStateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_state)

        val serviciosSeleccionados = intent.getStringArrayListExtra("servicios_seleccionados")
        val serviciosMutable = serviciosSeleccionados?.toMutableList() ?: mutableListOf()
        val volverMainButton: Button = findViewById(R.id.volverMainButton)
        val listView: ListView = findViewById(R.id.serviciosRealizadosListView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_checked, serviciosMutable)
        listView.adapter = adapter

        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        // se marcan los servicios realizados

        volverMainButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
