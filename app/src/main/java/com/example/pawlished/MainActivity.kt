package com.example.pawlished

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val solicitarCorteButton: Button = findViewById(R.id.solicitarCorteButton)
        solicitarCorteButton.setOnClickListener {
            val intent = Intent(this, SolicitarCorteActivity::class.java)
            startActivity(intent)
        }

        val viewStateButton: Button = findViewById(R.id.viewStateButton)
        viewStateButton.setOnClickListener {
            val intent = Intent(this, ViewStateActivity::class.java)
            startActivity(intent)
        }

        val viewNearestButton: Button = findViewById(R.id.viewNearestButton)
        viewNearestButton.setOnClickListener {
            val intent = Intent(this, ViewNearestActivity::class.java)
            startActivity(intent)
        }
    }
}
