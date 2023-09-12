package com.example.pawlished

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.TextView
import com.example.pawlished.R

class ServiciosAceptadosAdapter(
    private val context: Context,
    private val servicios: List<String>
) : BaseAdapter() {

    private val preciosPropuestos = mutableMapOf<String, Double>()

    override fun getCount(): Int {
        return servicios.size
    }

    override fun getItem(position: Int): Any {
        return servicios[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = inflater.inflate(R.layout.item_servicio_aceptado, parent, false)

        val servicioTextView = rowView.findViewById<TextView>(R.id.servicioTextView)
        val precioEditText = rowView.findViewById<EditText>(R.id.precioEditText)

        val servicio = servicios[position]
        servicioTextView.text = servicio

        // Verificar si ya existe un precio propuesto para este servicio
        if (preciosPropuestos.containsKey(servicio)) {
            precioEditText.setText(preciosPropuestos[servicio].toString())
        }

        precioEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // Cuando el EditText pierde el foco, guarda el precio propuesto
                val precio = precioEditText.text.toString().toDoubleOrNull()
                if (precio != null) {
                    preciosPropuestos[servicio] = precio
                }
            }
        }

        return rowView
    }

    // Obtener los precios propuestos para todos los servicios
    fun getPreciosPropuestos(): Map<String, Double> {
        return preciosPropuestos
    }
}
