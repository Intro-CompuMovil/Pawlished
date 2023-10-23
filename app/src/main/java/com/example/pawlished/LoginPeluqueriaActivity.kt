package com.example.pawlished

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseUser

class LoginPeluqueriaActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var correoEditText: EditText
    private lateinit var contraseñaEditText: EditText
    private lateinit var iniciarSesionButton: Button
    private lateinit var registrarPeluqueriaButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_peluqueria)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        correoEditText = findViewById(R.id.emailEditText)
        contraseñaEditText = findViewById(R.id.passwordEditText)
        iniciarSesionButton = findViewById(R.id.iniciarSesionPeluqueriaButton)
        registrarPeluqueriaButton = findViewById(R.id.registrarPeluqueriaButton)

        iniciarSesionButton.setOnClickListener {
            val correo = correoEditText.text.toString()
            val contraseña = contraseñaEditText.text.toString()

            if (correo.isNotEmpty() && contraseña.isNotEmpty()) {
                auth.signInWithEmailAndPassword(correo, contraseña)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user: FirebaseUser? = auth.currentUser
                            if (user != null) {
                                val uid = user.uid
                                val userRef = db.collection("peluquerias").document(uid)

                                userRef.get()
                                    .addOnSuccessListener { document ->
                                        if (document != null && document.exists()) {
                                            val tipoUsuario = document.getString("tipoUsuario")

                                            if (tipoUsuario == "Peluqueria") {
                                                // Inicio de sesión exitoso para peluquería, ir a MainActivityPeluqueria
                                                val intent =
                                                    Intent(this@LoginPeluqueriaActivity, MainActivityPeluqueria::class.java)
                                                startActivity(intent)
                                                finish()
                                            } else {
                                                // Tipo de usuario incorrecto, mostrar mensaje y redirigir
                                                auth.signOut() // Cerrar la sesión
                                                Toast.makeText(
                                                    this@LoginPeluqueriaActivity,
                                                    "No tienes permiso para acceder a esta área.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                val intent1 = Intent(this@LoginPeluqueriaActivity, LoginClienteActivity::class.java)
                                                startActivity(intent1)
                                                finish()
                                            }
                                        } else {
                                            // El documento no existe, lo que significa que el usuario no está en Firestore
                                            Toast.makeText(
                                                this@LoginPeluqueriaActivity,
                                                "El usuario no existe.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        // Manejar errores de Firestore aquí
                                        Toast.makeText(
                                            this@LoginPeluqueriaActivity,
                                            "Error al acceder a Firestore: $e",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                        } else {
                            // El inicio de sesión falló, mostrar un mensaje de error
                            val exception = task.exception
                            if (exception != null) {
                                // Manejar errores de autenticación
                                Toast.makeText(
                                    this@LoginPeluqueriaActivity,
                                    "Inicio de sesión fallido. Verifica tus credenciales.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
            } else {
                Toast.makeText(
                    this@LoginPeluqueriaActivity,
                    "Por favor, completa todos los campos.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        registrarPeluqueriaButton.setOnClickListener {
            val intent = Intent(this, RegistroPeluqueriaActivity::class.java)
            startActivity(intent)
        }

        val boton_regresar = findViewById<Button>(R.id.volverMainButton)

        boton_regresar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
