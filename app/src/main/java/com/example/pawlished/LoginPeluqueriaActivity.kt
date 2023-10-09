package com.example.pawlished

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class LoginPeluqueriaActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var correoEditText: EditText
    private lateinit var contraseñaEditText: EditText
    private lateinit var iniciarSesionButton: Button
    private lateinit var registrarPeluqueriaButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_peluqueria)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference.child("peluquerias")

        correoEditText = findViewById(R.id.emailEditText)
         contraseñaEditText = findViewById(R.id.passwordEditText)
         iniciarSesionButton = findViewById(R.id.iniciarSesionPeluqueriaButton)
         registrarPeluqueriaButton  = findViewById(R.id.registrarPeluqueriaButton)

        iniciarSesionButton.setOnClickListener {
            val correo = correoEditText.text.toString()
            val contraseña = contraseñaEditText.text.toString()

            if (correo.isNotEmpty() && contraseña.isNotEmpty()) {
                auth.signInWithEmailAndPassword(correo, contraseña)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Verificar el tipo de usuario después del inicio de sesión
                            val user = auth.currentUser
                            if (user != null) {
                                // Obtener el tipo de usuario desde la base de datos
                                val uid = user.uid
                                val userRef = databaseReference.child(uid)

                                userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        val tipoUsuario =
                                            dataSnapshot.child("tipoUsuario").getValue(String::class.java)

                                        if (tipoUsuario == "Peluqueria") {
                                            // Inicio de sesión exitoso para peluquería, ir a MainActivityPeluqueria
                                            val intent =
                                                Intent(this@LoginPeluqueriaActivity, MainActivityPeluqueria::class.java)
                                            startActivity(intent)
                                            finish()
                                        } else {
                                            // Tipo de usuario incorrecto, mostrar mensaje y redirigir
                                            Toast.makeText(
                                                this@LoginPeluqueriaActivity,
                                                "No tienes permiso para acceder a esta área.seras redirigido",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            val intent1 = Intent(this@LoginPeluqueriaActivity, LoginClienteActivity::class.java)
                                            startActivity(intent1)
                                            finish()

                                        }
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {
                                        // Manejar errores de base de datos aquí
                                    }
                                })
                            }
                        } else {
                            // El inicio de sesión falló, muestra un mensaje de error
                            Toast.makeText(
                                this@LoginPeluqueriaActivity,
                                "Inicio de sesión fallido. Verifica tus credenciales.",
                                Toast.LENGTH_SHORT
                            ).show()
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
    }
}
