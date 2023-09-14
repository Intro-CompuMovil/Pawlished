package com.example.pawlished

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LoginClienteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_cliente)

        val iniciarSesionButton: Button = findViewById(R.id.iniciarSesionButton)
        val registrarClienteButton: Button = findViewById(R.id.registrarClienteButton)

        iniciarSesionButton.setOnClickListener {

            // Si las credenciales son válidas, se puede iniciar la siguiente actividad
            val intent = Intent(this, MainActivityCliente::class.java)
            startActivity(intent)
        }

        registrarClienteButton.setOnClickListener {
            val intent = Intent(this, RegistroClienteActivity::class.java)
            startActivity(intent)
        }
    }
}
