package com.example.pawlished

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView

class VerEstadoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_estado)

        val serviciosSeleccionados = intent.getStringArrayListExtra("servicios_seleccionados")
        val serviciosMutable = serviciosSeleccionados?.toMutableList() ?: mutableListOf()
        val volverMainButton: Button = findViewById(R.id.volverMainButton)
        val listView: ListView = findViewById(R.id.serviciosRealizadosListView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_checked, serviciosMutable)
        listView.adapter = adapter

        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        volverMainButton.setOnClickListener {
            val intent = Intent(this, MainActivityCliente::class.java)
            startActivity(intent)
            finish()
        }
    }
}
