package com.example.pawlished

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView

class VerSolicitudesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_solicitudes)

        // Aquí obtén la lista de solicitudes de clientes (debe implementarse esta lógica)
        val solicitudes = obtenerSolicitudesClientes()

        val listView: ListView = findViewById(R.id.solicitudesListView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, solicitudes)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            // Acción cuando se selecciona una solicitud de cliente
            val solicitudSeleccionada = solicitudes[position]
            // Puedes agregar lógica adicional aquí, como confirmar o rechazar la solicitud.
        }

        val verSolicitudesAceptadasButton: Button = findViewById(R.id.verSolicitudesAceptadasButton)
        verSolicitudesAceptadasButton.setOnClickListener {
            // Acción cuando se presiona el botón para ver solicitudes aceptadas (debe implementarse)
            // Puedes abrir la actividad para ver las solicitudes aceptadas aquí.
        }

        val volverMainButton: Button = findViewById(R.id.volverMainButton)
        volverMainButton.setOnClickListener {
            val intent = Intent(this, MainActivityPeluqueria::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Ejemplo de función para obtener las solicitudes de clientes (debe implementarse)
    private fun obtenerSolicitudesClientes(): List<String> {
        // Aquí se debería obtener la lista de solicitudes de clientes desde algún origen de datos.
        // Este es solo un ejemplo de lista ficticia.
        return listOf(
            "Solicitud 1 - Raza: Labrador, Servicios: Baño, Corte, Dirección: Calle 123",
            "Solicitud 2 - Raza: Poodle, Servicios: Baño, Dirección: Calle 456",
            "Solicitud 3 - Raza: Bulldog, Servicios: Corte, Dirección: Calle 789"
        )
    }
}
