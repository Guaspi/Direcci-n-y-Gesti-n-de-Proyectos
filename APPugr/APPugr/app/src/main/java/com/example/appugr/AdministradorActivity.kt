package com.example.appugr

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage

class AdministradorActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private val PICK_IMAGE = 1 // Código para seleccionar imagen
    private var imageUri: Uri? = null
    val db= Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_administrador)
        imageView = findViewById(R.id.imageView)


        val etNombreAlumno = findViewById<EditText>(R.id.etNombreAlumno)
        val btnSeleccionarImagen = findViewById<Button>(R.id.btnSeleccionarImagen)
        val spinnerAccesibilidad = findViewById<Spinner>(R.id.spinnerAccesibilidad)
        val btnDarDeAlta = findViewById<Button>(R.id.btnDarDeAlta)

        // Configurar opciones del Spinner
        val accesibilidadOptions = arrayOf("Baja visión", "Auditiva", "Movilidad reducida", "Ninguna")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, accesibilidadOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAccesibilidad.adapter = adapter

        // Configurar botón para seleccionar imagen
        btnSeleccionarImagen.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE)
        }


        // Configurar Spinner de Patrón 1
        val spinnerPatron1: Spinner = findViewById(R.id.spinner_patron1)
        val opcionesPatron = arrayOf("buho", "cerdo", "conejo", "gato", "loro", "tortuga")
        val adapterPatron1 = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesPatron)
        adapterPatron1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPatron1.adapter = adapterPatron1

        // Configurar Spinner de Patrón 2
        val spinnerPatron2: Spinner = findViewById(R.id.spinner_patron2)
        val adapterPatron2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesPatron)
        adapterPatron2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPatron2.adapter = adapterPatron2



        // Configurar botón para dar de alta
        btnDarDeAlta.setOnClickListener {
            val nombreAlumno = etNombreAlumno.text.toString()
            val tipoAccesibilidad = spinnerAccesibilidad.selectedItem.toString()
            val patron1 = spinnerPatron1.selectedItem.toString()
            val patron2 = spinnerPatron2.selectedItem.toString()

            if (imageUri != null && nombreAlumno.isNotBlank() && tipoAccesibilidad.isNotBlank()) {
                registrarAlumnoLocalmente(nombreAlumno, tipoAccesibilidad, patron1, patron2, imageUri)
                Toast.makeText(this, "usuarioDadoDeAlta.", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(this, "Por favor, completa todos los campos y selecciona una imagen.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registrarAlumnoLocalmente(
        nombre: String,
        accesibilidad: String,
        patron1: String,
        patron2: String,
        imageUri: Uri?
    ) {
        val imagenUrl = imageUri?.toString() ?: "sin_imagen" // Guarda la URI local como String

        val alumno = hashMapOf(
            "nombre" to nombre,
            "accesibilidad" to accesibilidad,
            "patron1" to patron1,
            "patron2" to patron2,
            "imagenUrl" to imagenUrl
        )

        registrarAlumnoEnFirebase(alumno, nombre) // Guardar en Firebase Firestore

        Toast.makeText(this, "Usuario registrado localmente.", Toast.LENGTH_SHORT).show()
    }


    private fun registrarAlumnoEnFirebase(alumno: HashMap<String, String>, nombre: String) {

        db.collection("alumnos").document(nombre.toString()).set(alumno)
            .addOnSuccessListener {
                Toast.makeText(this, "Alumno registrado con éxito.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al registrar alumno: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }


    // Manejar el resultado de la selección de imagen
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            imageUri = data?.data
            // Mostrar la imagen seleccionada en un ImageView
            imageView.setImageURI(imageUri) // Esto ahora funcionará
        }
    }
}
