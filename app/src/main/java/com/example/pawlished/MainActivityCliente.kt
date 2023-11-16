package com.example.pawlished

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth

class MainActivityCliente : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_cliente)
        val serviciosSeleccionados = intent.getStringArrayListExtra("servicios_seleccionados")
        val solicitarCorteButton: Button = findViewById(R.id.solicitarCorteButton)
        val verOfertasButton: Button = findViewById(R.id.verOfertas)
        val verPeluqueriasButton: Button = findViewById(R.id.verPeluqueriasButton)
        val toolbar= findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        mAuth = FirebaseAuth.getInstance()

        solicitarCorteButton.setOnClickListener {
            val intent = Intent(this, SolicitarCorteActivity::class.java)
            startActivity(intent)
        }


         verOfertasButton.setOnClickListener{
             val intent = Intent(this, VerOfertasActivity::class.java)
             intent.putStringArrayListExtra("servicios_seleccionados", serviciosSeleccionados)
             startActivity(intent)
         }
        verPeluqueriasButton.setOnClickListener {
            val intent = Intent(this, VerPeluqueriasActivity::class.java)
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
