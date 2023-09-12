package com.example.pawlished

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView

class VerSolicitudesAceptadasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_solicitudes_aceptadas)

        // Aquí obtén la lista de solicitudes aceptadas por la peluquería (debe implementarse esta lógica)
        val solicitudesAceptadas = obtenerSolicitudesAceptadas()

        val listView: ListView = findViewById(R.id.solicitudesAceptadasListView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, solicitudesAceptadas)
        listView.adapter = adapter

        val precioEditText: EditText = findViewById(R.id.precioEditText)
        val guardarPrecioButton: Button = findViewById(R.id.guardarPrecioButton)

        listView.setOnItemClickListener { _, _, position, _ ->
            // Acción cuando se selecciona una solicitud aceptada
            val solicitudSeleccionada = solicitudesAceptadas[position]
            // Puedes agregar lógica adicional aquí, como establecer el precio de la solicitud.
        }

        guardarPrecioButton.setOnClickListener {
            // Acción cuando se presiona el botón para guardar el precio (debe implementarse)
            val precioIngresado = precioEditText.text.toString()
            // Puedes guardar el precio ingresado en la solicitud seleccionada o en algún otro lugar.
        }

        val volverMainButton: Button = findViewById(R.id.volverMainButton)
        volverMainButton.setOnClickListener {
            val intent = Intent(this, MainActivityPeluqueria::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Ejemplo de función para obtener las solicitudes aceptadas por la peluquería (debe implementarse)
    private fun obtenerSolicitudesAceptadas(): List<String> {
        // Aquí se debería obtener la lista de solicitudes aceptadas desde algún origen de datos.
        // Este es solo un ejemplo de lista ficticia.
        return listOf(
            "Solicitud Aceptada 1 - Raza: Labrador, Servicios: Baño, Corte, Dirección: Calle 123",
            "Solicitud Aceptada 2 - Raza: Poodle, Servicios: Baño, Dirección: Calle 456",
            "Solicitud Aceptada 3 - Raza: Bulldog, Servicios: Corte, Dirección: Calle 789"
        )
    }
}
