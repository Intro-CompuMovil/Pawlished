package com.example.pawlished


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val iniciarSesionClienteButton: Button = findViewById(R.id.iniciarSesionClienteButton)
        iniciarSesionClienteButton.setOnClickListener {
            val intent = Intent(this, LoginClienteActivity::class.java)
            startActivity(intent)
        }

        val iniciarSesionPeluqueriaButton: Button = findViewById(R.id.iniciarSesionPeluqueriaButton)
        iniciarSesionPeluqueriaButton.setOnClickListener {
            val intent = Intent(this, LoginPeluqueriaActivity::class.java)
            startActivity(intent)
        }
    }
}
