package com.example.pawlished

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.provider.MediaStore
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegistroPeluqueriaActivity : AppCompatActivity() {
    companion object {
        private const val CAMERA_PERMISSION_REQUEST = 101
        private const val IMAGE_CAPTURE_REQUEST = 102
        private const val GALLERY_PERMISSION_REQUEST = 103
        private const val IMAGE_PICK_REQUEST = 104
    }

    private lateinit var peluqueriaImageView: ImageView
    private lateinit var correoEditText: EditText
    private lateinit var nombreEditText: EditText
    private lateinit var telefonoEditText: EditText
    private lateinit var contraseñaEditText: EditText
    private lateinit var tomarFotoButton: Button
    private lateinit var elegirImagenButton: Button
    private lateinit var registrarPeluqueriaButton: Button

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_peluqueria)

        peluqueriaImageView = findViewById(R.id.peluqueriaImageView)

        correoEditText = findViewById(R.id.correoEditText)
        nombreEditText = findViewById(R.id.nombreEditText)
        telefonoEditText = findViewById(R.id.telefonoEditText)
        contraseñaEditText = findViewById(R.id.passwordEditText)

        tomarFotoButton = findViewById(R.id.tomarFotoButton)
        elegirImagenButton = findViewById(R.id.elegirImagenButton)
        registrarPeluqueriaButton = findViewById(R.id.registroButton)

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()

        tomarFotoButton.setOnClickListener {
            if (checkCameraPermission()) {
                launchCamera()
            } else {
                requestCameraPermission()
            }
        }

        elegirImagenButton.setOnClickListener {
            // Crear un intent para abrir la galería de imágenes
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

            // Comprobar si la aplicación tiene permiso para acceder a la galería de imágenes
            if (checkGalleryPermission()) {
                startActivityForResult(intent, IMAGE_PICK_REQUEST)
            } else {
                requestGalleryPermission()
            }
        }

        registrarPeluqueriaButton.setOnClickListener {
            // Obtener los datos del formulario de registro
            val correo = correoEditText.text.toString().trim()
            val nombre = nombreEditText.text.toString()
            val telefono = telefonoEditText.text.toString().trim()
            val contraseña = contraseñaEditText.text.toString()

            // Registrar la peluquería en Firebase Authentication
            mAuth.createUserWithEmailAndPassword(correo, contraseña).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // La peluquería se registró correctamente en Firebase Authentication
                    val peluqueriaId = mAuth.currentUser?.uid

                    // Crear un nodo para la peluquería en la base de datos
                    val peluqueriaRef = peluqueriaId?.let { it1 ->
                        mDatabase.reference.child("peluquerias").child(it1)
                    }
                    val peluqueriaData = HashMap<String, Any>()
                    peluqueriaData["correo"] = correo
                    peluqueriaData["nombre"] = nombre
                    peluqueriaData["tipoUsuario"] = "Peluqueria"
                    peluqueriaData["numeroTelefono"] = telefono

                    // Guardar los datos de la peluquería en la base de datos
                    if (peluqueriaRef != null) {
                        peluqueriaRef.setValue(peluqueriaData)
                    }

                    // Redirigir a la peluquería a la actividad principal de peluquerías
                    val intent = Intent(this, MainActivityPeluqueria::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Hubo un error al registrar la peluquería en Firebase Authentication
                    // Manejar el error apropiadamente
                }
            }
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
            GALLERY_PERMISSION_REQUEST
        )
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
            peluqueriaImageView.setImageBitmap(imageBitmap)
        } else if (requestCode == IMAGE_PICK_REQUEST && resultCode == RESULT_OK) {
            val selectedImageUri = data?.data
            peluqueriaImageView.setImageURI(selectedImageUri)
        }
    }
}
