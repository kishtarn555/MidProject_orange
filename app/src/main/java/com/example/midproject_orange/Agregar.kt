package com.example.midproject_orange

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.sqliteproject.SQLiteHelper

class Agregar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar)

        val btnGuardar=findViewById<Button>(R.id.btnGuardar)
        btnGuardar.setOnClickListener {
            btnAction();
        }
    }
    fun btnAction() {
        val nombre=findViewById<EditText>(R.id.txtNombre)
        val precio=findViewById<EditText>(R.id.txtPrecio)
        val cantidad=findViewById<EditText>(R.id.txtCantidad)
        if (
            nombre.text.toString().length == 0
            || precio.text.toString().toFloatOrNull() == null
            || cantidad.text.toString().toIntOrNull() == null
        ) {
            Toast.makeText(this, "Campos invalidos", Toast.LENGTH_LONG)
                .show();
            return;
        }
        Intent(this,MainActivity::class.java).also {

            val nombreVal=nombre.text.toString()
            val precioVal=precio.text.toString().toFloat()
            val cantidadVal=cantidad.text.toString().toInt()

            val sqLiteHelper = SQLiteHelper(this)
            sqLiteHelper.insert(nombreVal, precioVal, cantidadVal)
            //it.putExtra("nombre",nombreVal)
            //it.putExtra("precio",precioVal)
            //it.putExtra("cantidad",cantidadVal)
            Toast.makeText(this, "Producto insertado", Toast.LENGTH_LONG)
                .show();
            startActivity(it)
        }
    }
}