package com.example.pawlished

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class ViewNearestActivity : AppCompatActivity() {

    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_nearest)

        val volverMainButton: Button = findViewById(R.id.volverMainButton)
        val peluquerias = arrayOf(
            "veterinaria la perilla \nDirección: Cra. 7 #126a-35\nHorario: 8:00 AM - 4:30 PM",
            "La pelu \nDirección: Cra. 11a#119-16\nHorario: 7:00 AM - 7:00 PM",
            "Nova vet+\nDirección: Cra.7c#125-45\nHorario: 9:00 AM - 5:00 PM"
        )

        val listView: ListView = findViewById(R.id.peluqueriasListView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, peluquerias)
        listView.adapter = adapter

        // Verificar si el permiso de ubicación está concedido
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Si no se concede, solicitar el permiso
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }

        volverMainButton.setOnClickListener {
            val intent = Intent(this, MainActivityCliente::class.java)
            startActivity(intent)
            finish()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            // Obtén la peluquería seleccionada
            val selectedPeluqueria = peluquerias[position]

            // Obtén los servicios seleccionados que pasaste desde SolicitarCorteActivity
            val serviciosSeleccionados =
                intent.getStringArrayListExtra("servicios_seleccionados")

            // Iniciar ViewStateActivity y pasar los datos
            val intent = Intent(this, ViewStateActivity::class.java)
            intent.putStringArrayListExtra("servicios_seleccionados", serviciosSeleccionados)
            intent.putExtra("selected_peluqueria", selectedPeluqueria)
            startActivity(intent)
        }
        val mapa: Button = findViewById(R.id.mapa)
        mapa.setOnClickListener {
            val intent = Intent(this, MapaActivity::class.java)
            startActivity(intent)
        }
    }
}
