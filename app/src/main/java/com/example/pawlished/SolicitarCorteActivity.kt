package com.example.pawlished

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SolicitarCorteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solicitar_corte)

        val volverMainButton: Button = findViewById(R.id.volverMainButton)
        val solicitarButton: Button = findViewById(R.id.solicitarButton)
        val serviciosCheckboxList = listOf(
            findViewById<CheckBox>(R.id.bañoCheckBox),
            findViewById<CheckBox>(R.id.corteCheckBox),
            findViewById<CheckBox>(R.id.deslanadoCheckBox),
            findViewById<CheckBox>(R.id.BañoEspecialCheckBox),
            findViewById<CheckBox>(R.id.uñasCheckBox),
            findViewById<CheckBox>(R.id.cuidadoOidosChexkBox),
            findViewById<CheckBox>(R.id.accesoriosCheckBox),
            findViewById<CheckBox>(R.id.fraganciasCheckBox)
        )

        solicitarButton.setOnClickListener {
            val serviciosSeleccionados = obtenerServiciosSeleccionados(serviciosCheckboxList)

            if (serviciosSeleccionados.isNotEmpty()) {
                // Pasar los servicios seleccionados a ViewNearestActivity
                val intent = Intent(this, ViewNearestActivity::class.java)
                intent.putStringArrayListExtra("servicios_seleccionados", serviciosSeleccionados)
                startActivity(intent)
            } else {
                Toast.makeText(this, "No has seleccionado ningún servicio", Toast.LENGTH_SHORT).show()
            }
        }

        volverMainButton.setOnClickListener {
            val intent = Intent(this, MainActivityCliente::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun obtenerServiciosSeleccionados(checkboxList: List<CheckBox>): ArrayList<String> {
        val serviciosSeleccionados = ArrayList<String>()
        for (checkBox in checkboxList) {
            if (checkBox.isChecked) {
                serviciosSeleccionados.add(checkBox.text.toString())
            }
        }
        return serviciosSeleccionados
    }
}
