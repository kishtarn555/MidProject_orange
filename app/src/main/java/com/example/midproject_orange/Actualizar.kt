package com.example.midproject_orange

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.sqliteproject.SQLiteHelper

class Actualizar : AppCompatActivity() {
    var sqlLiteHelper: SQLiteHelper? = null
    var txtNombre: EditText? = null
    var txtPrecio: EditText? = null
    var txtCantidad: EditText? = null
    var productId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar)

        sqlLiteHelper = SQLiteHelper(this)
        txtNombre = findViewById<EditText>(R.id.txtNombre)
        txtPrecio = findViewById<EditText>(R.id.txtPrecio)
        txtCantidad = findViewById<EditText>(R.id.txtCantidad)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)

        btnGuardar.setOnClickListener {
            btnAction()
        }

        productId = intent.getIntExtra("productId",-1)
        val product = sqlLiteHelper!!.getProduct(productId)

        if (product != null) {
            txtNombre!!.setText(product.name)
            txtPrecio!!.setText(product.price.toString())
            txtCantidad!!.setText(product.quantity.toString())
        } else {
            Toast.makeText(this, "No se pudo cargar el producto", Toast.LENGTH_LONG)
                .show()
            finish()
        }
    }

    fun btnAction() {
        if (
            txtNombre!!.text.toString().isEmpty()
            || txtPrecio!!.text.toString().toFloatOrNull() == null
            || txtCantidad!!.text.toString().toIntOrNull() == null
        ) {
            Toast.makeText(this, "Los campos no son válidos", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val nombreVal = txtNombre!!.text.toString()
        val precioVal = txtPrecio!!.text.toString().toFloat()
        val cantidadVal = txtCantidad!!.text.toString().toInt()

        if(cantidadVal == 0) {
            Toast.makeText(this, "La cantidad no es válida (es nula)", Toast.LENGTH_SHORT)
                .show()
            return
        }

        sqlLiteHelper!!.updateProduct(productId, nombreVal, precioVal, cantidadVal)

        Toast.makeText(this, "Producto actualizado exitosamente", Toast.LENGTH_LONG)
            .show()

        // Regresar a MainActivity con el result
        val resultIntent = Intent()
        resultIntent.putExtra("refresh", true)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}