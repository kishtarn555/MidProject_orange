package com.example.midproject_orange

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class Agregar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar)
        val nombre=findViewById<EditText>(R.id.txtNombre)
        val precio=findViewById<EditText>(R.id.txtPrecio)
        val cantidad=findViewById<EditText>(R.id.txtCantidad)
        var nombreVal=nombre.text.toString()
        var precioVal=precio.text.toString().toInt()
        var cantidadVal=cantidad.text.toString().toInt()
        val btnGuardar=findViewById<Button>(R.id.btnGuardar)
        btnGuardar.setOnClickListener {
            Intent(this,MainActivity::class.java).also {
                it.putExtra("nombre",nombreVal)
                it.putExtra("precio",precioVal)
                it.putExtra("cantidad",cantidadVal)
            }
        }
    }
}