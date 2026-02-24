package com.example.appugr

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import android.Manifest
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appugr.model.Alumno
import com.example.appugr.ui.theme.APPugrTheme
import com.google.firebase.firestore.FirebaseFirestore

class AlumnoActivity : AppCompatActivity() {

    private val REQUEST_PERMISSION_CODE = 1001
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AlumnoAdapter
    private val alumnosList = mutableListOf<Alumno>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alumno)
        checkPermissions()
        recyclerView = findViewById(R.id.recyclerAlumnos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Obtener alumnos de Firebase
        val db = FirebaseFirestore.getInstance()
        db.collection("alumnos").get()
            .addOnSuccessListener { result ->
                alumnosList.clear()
                for (document in result) {
                    val alumno = document.toObject(Alumno::class.java)
                    alumnosList.add(alumno)
                }
                // Crear el adapter con la lista y la función para manejar los clics
                adapter = AlumnoAdapter(alumnosList) { alumno ->
                    // Abrir la nueva pantalla con los datos del alumno
                    val intent = Intent(this, DetalleAlumnoActivity::class.java).apply {
                        putExtra("nombre", alumno.nombre)
                        putExtra("patron1", alumno.patron1)
                        putExtra("patron2", alumno.patron2)
                    }
                    startActivity(intent)
                }

                recyclerView.adapter = adapter
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al obtener alumnos", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_MEDIA_IMAGES), REQUEST_PERMISSION_CODE)
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSION_CODE)
            }
        }
    }
}