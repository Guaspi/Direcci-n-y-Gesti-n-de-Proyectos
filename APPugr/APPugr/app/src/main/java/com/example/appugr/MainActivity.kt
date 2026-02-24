package com.example.appugr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.appugr.AdministradorActivity
import com.example.appugr.AlumnoActivity
import com.example.appugr.R
import com.google.firebase.firestore.FirebaseFirestore



fun insertarAlumnoBasico() {
    // Obtener instancia de Firebase Firestore
    val db = FirebaseFirestore.getInstance()

    // Crear un mapa con los datos básicos del alumno
    val alumno = hashMapOf(
        "nombre" to "Juan Pérez",
        "accesibilidad" to "Baja visión",
        "patron1" to "Loro",
        "patron2" to "buho",
        "imagenUrl" to "....",


        )

    // Insertar el documento en la colección "alumnos"
    db.collection("alumnos").document("alumno X").set(alumno)

}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        insertarAlumnoBasico() // Llamada a la función de prueba



        // Referenciar los botones
        val btnAdministrador = findViewById<Button>(R.id.btnAdministrador)
        val btnAlumno = findViewById<Button>(R.id.btnAlumno)

        // Configurar clics en los botones
        btnAdministrador.setOnClickListener {
            val intent = Intent(this, LoginAdminActivity::class.java)
            startActivity(intent)
        }

        btnAlumno.setOnClickListener {
            val intent = Intent(this, AlumnoActivity::class.java)
            startActivity(intent)
        }
    }
}
