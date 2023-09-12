package com.example.pawlished

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class RegistroPeluqueriaActivity : AppCompatActivity() {

    private lateinit var nombreEditText: EditText
    private lateinit var direccionEditText: EditText
    private lateinit var descripcionEditText: EditText
    private lateinit var peluqueriaImageView: ImageView
    private lateinit var tomarFotoButton: Button
    private lateinit var registroButton: Button
    private var fotoTomada: Bitmap? = null

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 101
        private const val CAMERA_PERMISSION_REQUEST_CODE = 102
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_peluqueria)

        nombreEditText = findViewById(R.id.nombreEditText)
        direccionEditText = findViewById(R.id.direccionEditText)
        descripcionEditText = findViewById(R.id.descripcionEditText)
        peluqueriaImageView = findViewById(R.id.peluqueriaImageView)
        tomarFotoButton = findViewById(R.id.tomarFotoButton)
        registroButton = findViewById(R.id.registroButton)

        tomarFotoButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // El permiso de la cámara ya está concedido, inicia la cámara
                abrirCamara()
            } else {
                // Solicitar permiso de cámara
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_REQUEST_CODE
                )
            }
        }

        registroButton.setOnClickListener {
            // Aquí puedes implementar la lógica para guardar los datos y la foto de la peluquería
            // en tu base de datos o sistema de registro.
        }
    }

    private fun abrirCamara() {
        val intentTomarFoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intentTomarFoto, REQUEST_IMAGE_CAPTURE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso de cámara concedido, inicia la cámara
                abrirCamara()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Obtiene la foto tomada y la muestra en el ImageView
            fotoTomada = data?.extras?.get("data") as Bitmap
            peluqueriaImageView.setImageBitmap(fotoTomada)
            peluqueriaImageView.visibility = View.VISIBLE
        }
    }
}
