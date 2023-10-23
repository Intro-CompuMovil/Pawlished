package com.example.pawlished

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class SolicitudAdapter(context: Context, resource: Int, objects: MutableList<Solicitud>) :
    ArrayAdapter<Solicitud>(context, resource, objects) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var solicitudes = objects
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.item_solicitud, parent, false)
            viewHolder = ViewHolder()
            viewHolder.direccionTextView = view.findViewById(R.id.direccionTextView)
            viewHolder.descripcionTextView = view.findViewById(R.id.descripcionTextView)
            viewHolder.serviciosTextView = view.findViewById(R.id.serviciosTextView)
            viewHolder.ofertaButton = view.findViewById(R.id.ofertarButton)
            viewHolder.precioEditText=view.findViewById(R.id.precioEditTex)


            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val solicitud = solicitudes[position]
        viewHolder.direccionTextView.text = "Direccion: "+solicitud.direccion
        viewHolder.descripcionTextView.text = "Descripcion de la mascota: "+solicitud.descripcion
        viewHolder.serviciosTextView.text = "Servicios: "+solicitud.servicios.joinToString(", ")

        viewHolder.ofertaButton.setOnClickListener {
            // Obtén el ID de la solicitud y el precio ingresado
            val solicitudId = solicitud.id
            val precioOfertado = viewHolder.precioEditText.text.toString().trim()
            val currentUser = FirebaseAuth.getInstance().currentUser
            val userId = currentUser?.uid
            val firestore = FirebaseFirestore.getInstance()
            val usersCollection = firestore.collection("peluquerias")
            val userDocument = usersCollection.document(userId!!)
            userDocument.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val peluqueriaNombre = documentSnapshot.getString("nombre")

                        if (!solicitudId.isNullOrBlank() && !precioOfertado.isBlank()) {
                            // Escribe la oferta en la base de datos
                            val ofertaReference = databaseReference.child("ofertas").push()
                            val ofertaData = hashMapOf("solicitudId" to solicitudId, "precio" to precioOfertado,"Peluqueria" to peluqueriaNombre,"Estado" to "ofertada")
                            ofertaReference.setValue(ofertaData)

                            // Muestra un Toast
                            Toast.makeText(context, "Oferta realizada", Toast.LENGTH_SHORT).show()
                        }

                    }
                }
                .addOnFailureListener { e ->

                }


        }



        return view
    }

    internal class ViewHolder {
        lateinit var direccionTextView: TextView
        lateinit var descripcionTextView: TextView
        lateinit var serviciosTextView: TextView
        lateinit var ofertaButton: Button
        lateinit var precioEditText: EditText
    }

    fun setSolicitudes(solicitudes: MutableList<Solicitud>) {
        this.solicitudes = solicitudes
        notifyDataSetChanged()
    }

}

