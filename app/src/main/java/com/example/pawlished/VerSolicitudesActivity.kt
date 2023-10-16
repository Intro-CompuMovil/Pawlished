package com.example.pawlished

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class VerSolicitudesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_solicitudes)

        val volverMainButton: Button = findViewById(R.id.volverMainButton)
        val listView: ListView = findViewById(R.id.serviciosSolicitadosListView)

        // Supongamos que estos son los servicios seleccionados
        val serviciosSeleccionados = listOf("Servicio 1", "Servicio 2", "Servicio 3")

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, serviciosSeleccionados)
        listView.adapter = adapter

        volverMainButton.setOnClickListener {
            val intent = Intent(this, MainActivityPeluqueria::class.java)
            startActivity(intent)
            finish()
        }

        val verAceptadasButton: Button = findViewById(R.id.verAceptadasButton)
        verAceptadasButton.setOnClickListener {
            // Obtén los elementos seleccionados
            val itemsSeleccionados = mutableListOf<String>()
            val checkedItemPositions = listView.checkedItemPositions
            for (i in 0 until checkedItemPositions.size()) {
                val position = checkedItemPositions.keyAt(i)
                if (checkedItemPositions.valueAt(i)) {
                    itemsSeleccionados.add(serviciosSeleccionados[position])
                }
            }

            // Pasa los elementos seleccionados a VerSolicitudesAceptadasActivity
            val intent = Intent(this, VerSolicitudesAceptadasActivity::class.java)
            intent.putStringArrayListExtra("servicios_seleccionados", ArrayList(itemsSeleccionados))
            startActivity(intent)
        }
    }
}
