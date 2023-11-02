package com.example.midproject_orange

import android.app.Activity
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

        val btnGuardar = findViewById<Button>(R.id.btnGuardar)

        btnGuardar.setOnClickListener {
            btnGuardarAction()
        }
    }
    fun btnGuardarAction() {
        val txtNombre = findViewById<EditText>(R.id.txtNombre)
        val txtPrecio = findViewById<EditText>(R.id.txtPrecio)
        val txtCantidad = findViewById<EditText>(R.id.txtCantidad)

        // Validar campos del producto
        if (
            txtNombre.text.toString().isEmpty()
            || txtPrecio.text.toString().toFloatOrNull() == null
            || txtCantidad.text.toString().toIntOrNull() == null
        ) {
            Toast.makeText(this, "Los campos no son válidos", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val nombreVal = txtNombre.text.toString()
        val precioVal = txtPrecio.text.toString().toFloat()
        val cantidadVal = txtCantidad.text.toString().toInt()

        if(cantidadVal == 0) {
            Toast.makeText(this, "La cantidad no es válida (es nula)", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val dbHelper = SQLiteHelper(this)

        dbHelper.insert(nombreVal, precioVal, cantidadVal)

        Toast.makeText(this, "Producto agregado exitosamente", Toast.LENGTH_LONG)
            .show()

        // Regresar a MainActivity con el result
        val resultIntent = Intent()
        resultIntent.putExtra("refresh", true)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}