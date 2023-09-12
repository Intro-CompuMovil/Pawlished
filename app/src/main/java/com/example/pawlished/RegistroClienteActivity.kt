package com.example.pawlished

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class RegistroClienteActivity : AppCompatActivity() {

    companion object {
        private const val CAMERA_PERMISSION_REQUEST = 101
        private const val IMAGE_CAPTURE_REQUEST = 102
    }

    private lateinit var clienteImageView: ImageView
    private lateinit var nombreEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_cliente)

        clienteImageView = findViewById(R.id.clienteImageView)
        nombreEditText = findViewById(R.id.nombreEditText)
        val tomarFotoButton: Button = findViewById(R.id.tomarFotoButton)
        val registrarClienteButton: Button = findViewById(R.id.registrarClienteButton)

        tomarFotoButton.setOnClickListener {
            if (checkCameraPermission()) {
                launchCamera()
            } else {
                requestCameraPermission()
            }
        }

        registrarClienteButton.setOnClickListener {
            // Aquí puedes implementar la lógica para registrar al cliente en tu sistema
            val nombre = nombreEditText.text.toString()
            // También puedes guardar la imagen del cliente en tu sistema
            // clienteImageView.drawable.toBitmap() te proporciona el bitmap de la imagen
            Toast.makeText(this, "Cliente registrado: $nombre", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST
        )
    }

    private fun launchCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, IMAGE_CAPTURE_REQUEST)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchCamera()
            } else {
                Toast.makeText(
                    this,
                    "Permiso de cámara denegado. No se puede tomar la foto.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CAPTURE_REQUEST && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            clienteImageView.setImageBitmap(imageBitmap)
        }
    }
}
