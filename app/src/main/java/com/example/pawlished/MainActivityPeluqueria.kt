package com.example.pawlished

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivityPeluqueria : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_peluqueria)

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
}
