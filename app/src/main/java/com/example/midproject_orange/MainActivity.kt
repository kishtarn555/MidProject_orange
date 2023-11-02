package com.example.midproject_orange
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.sqliteproject.SQLiteHelper

class MainActivity : AppCompatActivity() {
    var sqlLiteHelper: SQLiteHelper? = null

    fun addFormatToProductRow(txtId: TextView, txtName: TextView, txtPrice: TextView, txtQuantity: TextView, isTitle: Boolean) {
        // Padding
        txtId.setPadding(10, 10, 10, 10)
        txtName.setPadding(10, 10, 10, 10)
        txtPrice.setPadding(20, 20, 20, 20)
        txtQuantity.setPadding(20, 20, 20, 20)

        // LayoutParams
        val params1 = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT, // width
            TableRow.LayoutParams.WRAP_CONTENT, // height
            0f
        )

        val params2 = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT, // width
            TableRow.LayoutParams.WRAP_CONTENT, // height
            1f // Share remaining horizontal space
        )

        txtId.layoutParams = params1
        txtName.layoutParams = params1
        txtPrice.layoutParams = params2
        txtQuantity.layoutParams = params2

        // Centrar horizontal y verticalmente
        txtId.gravity = Gravity.CENTER
        if(isTitle) txtName.gravity = Gravity.CENTER
        txtPrice.gravity = Gravity.CENTER
        txtQuantity.gravity = Gravity.CENTER
    }

    fun addColumnTitlesProductsTable(productosTableLayout: TableLayout) {
        val tableRow = TableRow(this)
        // Crear elementos de la fila
        val txtId = TextView(this)
        val txtName = TextView(this)
        val txtPrice = TextView(this)
        val txtQuantity = TextView(this)

        // Formato para los elementos de la fila
        addFormatToProductRow(txtId, txtName, txtPrice, txtPrice, true)

        txtId.text = "ID"
        txtName.text = "Producto"
        txtPrice.text = "Precio"
        txtQuantity.text = "Cantidad"

        tableRow.addView(txtId)
        tableRow.addView(txtName)
        tableRow.addView(txtPrice)
        tableRow.addView(txtQuantity)

        productosTableLayout.addView(tableRow)
    }

    fun createProductTableRow(product: Product): TableRow {
        val tableRow = TableRow(this)
        // Crear elementos de la fila
        val txtId = TextView(this)
        val txtName = TextView(this)
        val txtPrice = TextView(this)
        val txtQuantity = TextView(this)

        // Formato para los elementos de la fila
        addFormatToProductRow(txtId, txtName, txtPrice, txtPrice, false)

        txtId.text = product.id.toString()
        txtName.text = product.name
        txtPrice.text = product.price.toString()
        txtQuantity.text = product.quantity.toString()

        tableRow.addView(txtId)
        tableRow.addView(txtName)
        tableRow.addView(txtPrice)
        tableRow.addView(txtQuantity)

        return tableRow
    }

    fun fillProductsTable(productosTableLayout: TableLayout) {
        // Eliminar todos los elementos de la tabla
        productosTableLayout.removeAllViews()

        addColumnTitlesProductsTable(productosTableLayout)

        val products = sqlLiteHelper!!.getAllProducts()
        for(product in products) {
            val currentRow = createProductTableRow(product)
            productosTableLayout.addView(currentRow)
        }
    }

    fun productIdIsValid(productIdString: String): Boolean {
        if(productIdString.isEmpty()) return false
        val productId = productIdString.toIntOrNull()
        if(productId == null) return false
        return sqlLiteHelper!!.checkId(productId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sqlLiteHelper = SQLiteHelper(this)
        val productosTableLayout = findViewById<TableLayout>(R.id.productosTableLayout)

        val btnAgregar = findViewById<Button>(R.id.btnAgregar)
        val btnActualizar = findViewById<Button>(R.id.btnActualizar)
        val btnBorrar = findViewById<Button>(R.id.btnBorrar)

        val editTxtProductId = findViewById<EditText>(R.id.editTxtProductId)

        fillProductsTable(productosTableLayout)

        btnAgregar.setOnClickListener{
            Intent(this,Agregar::class.java).also{
                startActivityForResult(it, 0)
            }
        }

        btnActualizar.setOnClickListener {
            Intent(this,Actualizar::class.java).also {
                val productIdString = editTxtProductId.text.toString()
                // Validar ID del producto
                if(productIdIsValid(productIdString)) {
                    val productIdForUpdate = productIdString.toInt()
                    it.putExtra("productId", productIdForUpdate)
                    startActivityForResult(it, 0)
                } else {
                    Toast.makeText(this, "El ID del producto no es válido", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        btnBorrar.setOnClickListener{
            val productIdString = editTxtProductId.text.toString()
            // Validar ID del producto
            if(productIdIsValid(productIdString)) {
                val productIdForUpdate = productIdString.toInt()
                // Crear DialogBuilder para confirmar
                val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
                dialogBuilder
                    .setMessage("El producto será borrado inmediatamente")
                    .setTitle("¿Está seguro de que quiere borrar el producto?")
                    .setPositiveButton("Borrar") { dialog, which ->
                        // Delete from DB
                        sqlLiteHelper!!.deleteProduct(productIdForUpdate)

                        Toast.makeText(this, "El producto fue borrado exitosamente", Toast.LENGTH_SHORT)
                            .show()

                        // Recreate Activity
                        recreate()
                    }
                    .setNegativeButton("Cancelar") { dialog, which -> }
                // Crear dialog para confirmar
                val dialog: AlertDialog = dialogBuilder.create()
                dialog.show()
            } else {
                Toast.makeText(this, "El ID del producto no es válido", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    // For Intent Result
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            // Obtener refresh flag si data es null, refresh es false
            val refresh = data?.getBooleanExtra("refresh", false) ?: false
            if (refresh) {
                // Recreate activity
                recreate()
            }
        }
    }
}