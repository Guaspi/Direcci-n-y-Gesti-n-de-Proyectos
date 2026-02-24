package com.example.appugr

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class LoginAdminActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_admin)

        db = FirebaseFirestore.getInstance()

        val etUsuario = findViewById<EditText>(R.id.etUsuario)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val usuario = etUsuario.text.toString()
            val password = etPassword.text.toString()

            if (usuario.isNotEmpty() && password.isNotEmpty()) {
                validarAdministrador(usuario, password)
            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validarAdministrador(usuario: String, password: String) {
        val docRef = db.collection("administradores").document(usuario)

        docRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val codigo = document.getString("codigo")
                if (codigo == password) {
                    Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, AdministradorActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show()
        }
    }
}