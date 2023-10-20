package com.example.pawlished

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth

class MainActivityPeluqueria : AppCompatActivity() {
private lateinit var mAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_peluqueria)

        val toolbar= findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
         mAuth = FirebaseAuth.getInstance()
        val verSolicitudesButton: Button = findViewById(R.id.verSolicitudesButton)
        val verSolicitudesAceptadasButton: Button = findViewById(R.id.verSolicitudesAceptadasButton)

        verSolicitudesButton.setOnClickListener {
            val intent = Intent(this, VerSolicitudesActivity::class.java)
            startActivity(intent)
        }

        verSolicitudesAceptadasButton.setOnClickListener {
            val intent = Intent(this, VerSolicitudesAceptadasActivity::class.java)
            startActivity(intent)
        }
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
                val intent = Intent(this, LoginPeluqueriaActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
            R.id.menu_settings -> {
                val intent = Intent(this, SettingsActivityP::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
