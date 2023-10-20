package com.example.pawlished

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class SettingsActivityP : AppCompatActivity() {

    companion object {
        private const val CAMERA_PERMISSION_REQUEST = 101
        private const val IMAGE_CAPTURE_REQUEST = 102
        private const val GALLERY_PERMISSION_REQUEST = 103
        private const val IMAGE_PICK_REQUEST = 104
    }

    private lateinit var nombre: TextView
    private lateinit var correo: TextView
    private lateinit var contrasena: TextView
    private lateinit var telefono: TextView
    private lateinit var direccion: TextView
    private lateinit var foto: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_p)

        nombre = findViewById(R.id.nombreEditText)
        correo = findViewById(R.id.correoEditText)
        contrasena = findViewById(R.id.contrasenaEditText)
        telefono = findViewById(R.id.telefonoEditText)
        foto = findViewById(R.id.fotoPerfil)
        direccion = findViewById(R.id.direccionEditText)

        //Aqui mi idea es poner en los hints los datos que ya tiene el usuario
        nombre.hint = "Nombre: " + "nombre en base de datos"
        correo.hint = "Correo: " + "correo en base de datos"
        contrasena.hint = "Contraseña: " + "contraseña en base de datos"
        telefono.hint = "Telefono: " + "telefono en base de datos"

        //tambien poner en el image view la foto de perfil del usuario

        val tomarFotoButton: Button = findViewById(R.id.tomarFotoButton)

        tomarFotoButton.setOnClickListener {
            if (checkCameraPermission()) {
                launchCamera()
            } else {
                requestCameraPermission()
            }
        }

        val elegirImagenButton: Button = findViewById(R.id.elegirImagenButton)

        elegirImagenButton.setOnClickListener {
            // Crear un intent para abrir la galería de imágenes
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

            // Comprobar si la aplicación tiene permiso para acceder a la galería de imágenes
            if (checkGalleryPermission()) {
                startActivityForResult(intent, SettingsActivityP.IMAGE_PICK_REQUEST)
            } else {
                requestGalleryPermission()
            }
        }

        //Aqui se guardan los datos en la base de datos
        val botonGuardar: Button = findViewById(R.id.actualizarClienteButton)

        botonGuardar.setOnClickListener {
            //Aqui se guardan los datos en la base de datos


            Toast.makeText(this, "Datos actualizados", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivityPeluqueria::class.java)
            startActivity(intent)
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
            SettingsActivityP.CAMERA_PERMISSION_REQUEST
        )
    }
    private fun launchCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, SettingsActivityP.IMAGE_CAPTURE_REQUEST)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == SettingsActivityP.CAMERA_PERMISSION_REQUEST) {
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
        if (requestCode == SettingsActivityP.IMAGE_CAPTURE_REQUEST && resultCode == RESULT_OK)
        {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val rotatedBitmap = rotateImage(imageBitmap, 90f)
            foto.setImageBitmap(rotatedBitmap)
        }
        else if (requestCode == SettingsActivityP.IMAGE_PICK_REQUEST && resultCode == RESULT_OK)
        {
            val selectedImageUri = data?.data
            foto.setImageURI(selectedImageUri)
        }
    }

    private fun checkGalleryPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }
    private fun requestGalleryPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            SettingsActivityP.GALLERY_PERMISSION_REQUEST
        )
    }

    private fun rotateImage(source: Bitmap?, angle: Float): Bitmap? {
        if (source == null)
            return null

        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }
}