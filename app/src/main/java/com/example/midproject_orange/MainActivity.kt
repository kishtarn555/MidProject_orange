package com.example.midproject_orange
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnAgregar=findViewById<Button>(R.id.btnAgregar)
        btnAgregar.setOnClickListener{
            Intent(this,Agregar::class.java).also{
                startActivity(it)
            }
        }
        val nombreAgregar=intent.getStringExtra("nombre")
        val precioAgregar=intent.getIntExtra("precio",0)
        val cantidadAgregar=intent.getIntExtra("cantidad",0)
        val btnBorrar=findViewById<Button>(R.id.btnBorrar)
        btnBorrar.setOnClickListener{
            Intent(this,Borrar::class.java).also{
                startActivity(it)
            }
        }
        val btnActualizar=findViewById<Button>(R.id.btnActualizar)
        btnActualizar.setOnClickListener {
            Intent(this,Actualizar::class.java).also {
                startActivity(it)
            }
        }
    }
}