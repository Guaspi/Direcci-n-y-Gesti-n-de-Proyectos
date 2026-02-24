package com.example.appugr

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appugr.model.Alumno

class AlumnoAdapter(private val alumnos: List<Alumno>,
                    private val onItemClick: (Alumno) -> Unit
) : RecyclerView.Adapter<AlumnoAdapter.AlumnoViewHolder>() {

    class AlumnoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivAlumno: ImageView = view.findViewById(R.id.ivAlumno)
        val tvNombreAlumno: TextView = view.findViewById(R.id.tvNombreAlumno)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlumnoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_alumno, parent, false)
        return AlumnoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlumnoViewHolder, position: Int) {
        val alumno = alumnos[position]
        holder.tvNombreAlumno.text = alumno.nombre

        // Cargar imagen desde URI local
        holder.ivAlumno.setImageURI(Uri.parse(alumno.imagenUrl))

        // Hacer que el elemento sea pulsable
        holder.itemView.setOnClickListener {
            onItemClick(alumno)  // Ejecutar la acción cuando se pulsa el botón
        }
    }



    override fun getItemCount() = alumnos.size
}