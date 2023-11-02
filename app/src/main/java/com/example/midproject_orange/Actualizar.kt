package com.example.midproject_orange

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.sqliteproject.SQLiteHelper

class Actualizar : AppCompatActivity() {
    var nombre:EditText? = null
    var precio:EditText? = null
    var cantidad:EditText? = null
    var productId:Int=-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar)


        nombre  = findViewById<EditText>(R.id.txtNombre)
        precio  = findViewById<EditText>(R.id.txtPrecio)
        cantidad= findViewById<EditText>(R.id.txtCantidad)
        val btnGuardar=findViewById<Button>(R.id.btnGuardar)
        btnGuardar.setOnClickListener {
            btnAction();
        }


        productId=intent.getIntExtra("productID",-1);
        val sqlLiteHelper = SQLiteHelper(this);
        val product =  sqlLiteHelper.getProduct(productId);
        if (product != null) {

            nombre?.setText(product.name);
            precio?.setText(product.price.toString());
            cantidad?.setText(product.quantity.toString());
        } else {

            Toast.makeText(this, "No se pudo cargar el producto", Toast.LENGTH_LONG)
                .show();
            finish()
        }
    }

    fun btnAction() {
        val nombreVal=nombre!!.text.toString()
        val precioVal=precio!!.text.toString().toFloat()
        val cantidadVal=cantidad!!.text.toString().toInt()
        if (
            nombre!!.text.toString().length == 0
            || precio?.text.toString().toFloatOrNull() == null
            || cantidad?.text.toString().toIntOrNull() == null
        ) {
            Toast.makeText(this, "Campos invalidos", Toast.LENGTH_LONG)
                .show();
            return;
        }
        Intent(this,MainActivity::class.java).also {
            val sqlLiteHelper: SQLiteHelper = SQLiteHelper(this)
            sqlLiteHelper.updateProduct(productId, nombreVal, precioVal, cantidadVal)
            Toast.makeText(this, "Producto actualizado", Toast.LENGTH_LONG)
                .show();
            startActivity(it)
        }

    }
}