package com.example.pawlished

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivityCliente : AppCompatActivity() {
    private val serviciosSeleccionados: ArrayList<String> = ArrayList()
    private lateinit var mAuth: FirebaseAuth
    private lateinit var currentUserID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_cliente)

        val solicitarCorteButton: Button = findViewById(R.id.solicitarCorteButton)
        val verOfertasButton: Button = findViewById(R.id.verOfertas)
        val verPeluqueriasButton: Button = findViewById(R.id.verPeluqueriasButton)
        val toolbar= findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        mAuth = FirebaseAuth.getInstance()
        currentUserID = mAuth.currentUser?.uid ?: ""
        cargarSolicitudes()
        solicitarCorteButton.setOnClickListener {
            val intent = Intent(this, SolicitarCorteActivity::class.java)
            startActivity(intent)
        }


         verOfertasButton.setOnClickListener{
             cargarSolicitudes()

             val intent = Intent(this, VerOfertasActivity::class.java)
             intent.putStringArrayListExtra("servicios_seleccionados", serviciosSeleccionados)
             startActivity(intent)
         }
        verPeluqueriasButton.setOnClickListener {
            val intent = Intent(this, VerPeluqueriasActivity::class.java)
            startActivity(intent)
        }

    }

    private fun cargarSolicitudes() {
        val databaseReference = FirebaseDatabase.getInstance().reference
        val solicitudesReference = databaseReference.child("Solicitudes")

        solicitudesReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Itera sobre todas las solicitudes
                for (solicitudSnapshot in dataSnapshot.children) {
                    val userId = solicitudSnapshot.child("userId").getValue(String::class.java)
                    if(userId == currentUserID) {
                        for (i in 0 until solicitudSnapshot.childrenCount) {
                            val servicio =
                                solicitudSnapshot.child("servicio$i").getValue(String::class.java)
                            if (servicio != null && !serviciosSeleccionados.contains(servicio)) {
                                // Agrega el servicio a la lista si no está presente
                                serviciosSeleccionados.add(servicio)
                            }
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejo de errores si es necesario
            }
        })
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                // Aquí puedes agregar el código para cerrar la sesión del usuario
                mAuth.signOut()
                // Redirige al usuario a la actividad de inicio de sesión
                val intent = Intent(this, LoginClienteActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
            R.id.menu_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    }



