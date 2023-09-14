package com.example.pawlished

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LoginPeluqueriaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_peluqueria)

        val iniciarSesionPeluqueriaButton: Button = findViewById(R.id.iniciarSesionPeluqueriaButton)
        val registrarPeluqueriaButton: Button = findViewById(R.id.registrarPeluqueriaButton)

        iniciarSesionPeluqueriaButton.setOnClickListener {

            // Si las credenciales son válidas,  iniciar la siguiente actividad
            val intent = Intent(this, MainActivityPeluqueria::class.java)
            startActivity(intent)
        }

        registrarPeluqueriaButton.setOnClickListener {
            val intent = Intent(this, RegistroPeluqueriaActivity::class.java)
            startActivity(intent)
        }
    }
}
