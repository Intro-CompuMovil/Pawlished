package com.example.pawlished

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivityCliente : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_cliente)

        val solicitarCorteButton: Button = findViewById(R.id.solicitarCorteButton)
        val verEstadoButton: Button = findViewById(R.id.verEstadoButton)
        val verPeluqueriasButton: Button = findViewById(R.id.verPeluqueriasButton)

        solicitarCorteButton.setOnClickListener {
            val intent = Intent(this, SolicitarCorteActivity::class.java)
            startActivity(intent)
        }

        verEstadoButton.setOnClickListener {
            val intent = Intent(this, VerEstadoActivity::class.java)
            startActivity(intent)
        }

        verPeluqueriasButton.setOnClickListener {
            val intent = Intent(this, VerPeluqueriasActivity::class.java)
            startActivity(intent)
        }
    }
}
