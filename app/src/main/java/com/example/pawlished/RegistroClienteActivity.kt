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

class RegistroClienteActivity : AppCompatActivity() {
    companion object {
        private const val CAMERA_PERMISSION_REQUEST = 101
        private const val IMAGE_CAPTURE_REQUEST = 102
        private const val GALLERY_PERMISSION_REQUEST = 103
        private const val IMAGE_PICK_REQUEST = 104
    }

    private lateinit var clienteImageView: ImageView
    private lateinit var correoEditText: EditText
    private lateinit var nombreEditText: EditText
    private lateinit var telefonoEditText: EditText
    private lateinit var contraseñaEditText: EditText
    private lateinit var tomarFotoButton: Button
    private lateinit var elegirImagenButton: Button
    private lateinit var registrarClienteButton: Button


    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_cliente)

        clienteImageView = findViewById(R.id.clienteImageView)
        correoEditText = findViewById(R.id.correoEditText)
        nombreEditText = findViewById(R.id.nombreEditText)
        telefonoEditText = findViewById(R.id.telefonoEditText)
        contraseñaEditText= findViewById(R.id.contrasenaEditText)
        tomarFotoButton= findViewById(R.id.tomarFotoButton)
        elegirImagenButton = findViewById(R.id.elegirImagenButton)
        registrarClienteButton = findViewById(R.id.registrarClienteButton)


        tomarFotoButton.setOnClickListener {
            if (checkCameraPermission()) {
                launchCamera()
            } else {
                requestCameraPermission()
            }
        }


        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()

        registrarClienteButton.setOnClickListener {
            // Obtener los datos del formulario de registro
            val correo = correoEditText.text.toString().trim()
            val nombre = nombreEditText.text.toString()
            val telefono = telefonoEditText.text.toString().trim()
            val contraseña = contraseñaEditText.text.toString()


            // Registrar al cliente en Firebase Authentication
            mAuth.createUserWithEmailAndPassword(correo, contraseña).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // El cliente se registró correctamente en Firebase Authentication
                    val clienteId = mAuth.currentUser?.uid

                    // Crear un nodo para el cliente en la base de datos
                    val clienteRef = clienteId?.let { it1 ->
                        mDatabase.reference.child("clientes").child(
                            it1
                        )
                    }
                    val clienteData = HashMap<String, Any>()
                    clienteData["correo"] = correo
                    clienteData["nombre"] = nombre
                    clienteData["tipoUsuario"] = "Cliente"
                    clienteData["numeroTelefono"] = telefono

                    // Guardar los datos del cliente en la base de datos
                    if (clienteRef != null) {
                        clienteRef.setValue(clienteData)
                    }

                    // Redirigir al cliente a la actividad principal de clientes
                    val intent = Intent(this, MainActivityCliente::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Hubo un error al registrar al cliente en Firebase Authentication
                    // Manejar el error apropiadamente
                }
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
        } else if (requestCode == IMAGE_PICK_REQUEST && resultCode == RESULT_OK) {
            val selectedImageUri = data?.data
            clienteImageView.setImageURI(selectedImageUri)
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
            GALLERY_PERMISSION_REQUEST
        )
    }

}
