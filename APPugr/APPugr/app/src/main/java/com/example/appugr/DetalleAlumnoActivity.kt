package com.example.appugr

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class DetalleAlumnoActivity : AppCompatActivity() {
    private lateinit var botones: List<ImageButton>
    private lateinit var imgCheck1: ImageView
    private lateinit var imgCheck2: ImageView
    private var patronCorrecto = mutableListOf<String>()
    private var patronUsuario = mutableListOf<String>()
    private lateinit var alumnoNombre: String

    private val mapaAnimales = mapOf(
        R.id.btnbuho to "buho",
        R.id.btncerdo to "cerdo",
        R.id.btnconejo to "conejo",
        R.id.btngato to "gato",
        R.id.btnloro to "loro",
        R.id.btntortuga to "tortuga"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_alumno)

        alumnoNombre = intent.getStringExtra("nombre") ?: ""

        botones = mapaAnimales.keys.map { findViewById(it) }
        imgCheck1 = findViewById(R.id.imgCheck1)
        imgCheck2 = findViewById(R.id.imgCheck2)

        cargarPatronesDesdeFirebase()

        botones.forEach { boton ->
            boton.setOnClickListener {
                verificarSeleccion(boton.id)
            }
        }
    }

    private fun cargarPatronesDesdeFirebase() {
        val db = FirebaseFirestore.getInstance()
        db.collection("alumnos").document(alumnoNombre).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    patronCorrecto.add(document.getString("patron1") ?: "")
                    patronCorrecto.add(document.getString("patron2") ?: "")
                }
            }
    }

    private fun verificarSeleccion(idBoton: Int) {
        if (patronUsuario.size < 2) {
            val animalSeleccionado = mapaAnimales[idBoton] ?: return
            patronUsuario.add(animalSeleccionado)

            val index = patronUsuario.size - 1
            val correcto = patronUsuario[index] == patronCorrecto[index]

            if (correcto) {
                if (index == 0) {
                    imgCheck1.setImageResource(R.drawable.acierto)
                    imgCheck1.visibility = View.VISIBLE
                } else {
                    imgCheck2.setImageResource(R.drawable.acierto)
                    imgCheck2.visibility = View.VISIBLE
                }
            } else {
                if (index == 0) {
                    imgCheck1.setImageResource(R.drawable.fallo)
                    imgCheck1.visibility = View.VISIBLE
                } else {
                    imgCheck2.setImageResource(R.drawable.fallo)
                    imgCheck2.visibility = View.VISIBLE
                }
            }

            // Si ya seleccionó dos, verificar si es correcto
            if (patronUsuario.size == 2) {
                if (patronUsuario == patronCorrecto) {
                    // ✅ Correcto → Ir a otra pantalla
                    startActivity(Intent(this, ResultadoActivity::class.java))
                    finish()
                } else {
                    // ❌ Incorrecto → Esperar 5 segundos y reiniciar
                    bloquearBotones()
                    Handler(Looper.getMainLooper()).postDelayed({
                        reiniciarIntento()
                    }, 5000)
                }
            }
        }
    }

    private fun bloquearBotones() {
        botones.forEach { it.isEnabled = false }
    }

    private fun reiniciarIntento() {
        patronUsuario.clear()
        imgCheck1.visibility = View.GONE
        imgCheck2.visibility = View.GONE
        botones.forEach { it.isEnabled = true }
    }
}