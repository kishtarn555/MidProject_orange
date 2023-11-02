package com.example.midproject_orange
import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
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
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

class MainActivity : AppCompatActivity() {
    var sqlLiteHelper: SQLiteHelper? = null
    var productosTableLayout: TableLayout? = null
    var editTxtProductId: EditText? = null

     fun createTextView(text: String): TextView {
        val textView = TextView(this)
        textView.text = text
        return textView
    }

    // Formatea float como string del tipo de moneda en currencyCode
    fun formatFloatAsCurrency(number: Float, currencyCode: String): String {
        // Crear formateador con Locale default del sistema
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
        currencyFormat.currency = Currency.getInstance(currencyCode)

        val formattedFloat = currencyFormat.format(number) // ejemplo: MX$1,200.20
        val currencySymbol = Currency.getInstance(currencyCode).symbol
        // Borrar "MX$" de la string (reemplazarlo por string vacia) y le agregamos el "$" al principio
        val stringWithoutSymbol = "$" + formattedFloat.replace(currencySymbol, "")
        return stringWithoutSymbol
    }

    fun addFormatToProductRow(txtId: TextView, txtName: TextView, txtPrice: TextView, txtQuantity: TextView, isTitle: Boolean) {
        // Padding
        txtId.setPadding(10, 10, 10, 20)
        txtName.setPadding(10, 10, 10, 20)
        txtPrice.setPadding(15, 10, 10, 20)
        txtQuantity.setPadding(15, 10, 10, 20)

        // LayoutParams
        val params = TableRow.LayoutParams(
            0, // width (es 0 para que los campos se adapten al espacio)
            TableRow.LayoutParams.MATCH_PARENT, // height
        )

        txtId.layoutParams = params
        txtName.layoutParams = params
        txtPrice.layoutParams = params
        txtQuantity.layoutParams = params

        // Centrar horizontal y verticalmente
        txtId.gravity = Gravity.CENTER
        if(isTitle) txtName.gravity = Gravity.CENTER
        txtPrice.gravity = Gravity.CENTER
        txtQuantity.gravity = Gravity.CENTER
    }

    fun addColumnTitlesProductsTable(productosTableLayout: TableLayout) {
        val tableRow = TableRow(this)
        // Crear elementos de la fila
        val txtId = createTextView("ID")
        val txtName = createTextView("Producto")
        val txtPrice = createTextView("Precio")
        val txtQuantity = createTextView("Cantidad")

        // Formato para los elementos de la fila
        addFormatToProductRow(txtId, txtName, txtPrice, txtQuantity, true)

        // Texto en negritas
        txtId.setTypeface(null, Typeface.BOLD)
        txtName.setTypeface(null, Typeface.BOLD)
        txtPrice.setTypeface(null, Typeface.BOLD)
        txtQuantity.setTypeface(null, Typeface.BOLD)

        tableRow.addView(txtId)
        tableRow.addView(txtName)
        tableRow.addView(txtPrice)
        tableRow.addView(txtQuantity)

        productosTableLayout.addView(tableRow)
    }

    // Detectar click en fila de la tabla
    fun setProductTableRowClickListener(tableRow: TableRow, txtId: TextView) {
        tableRow.setOnClickListener {
            // Poner el ID del producto que se clickeo en el campo de ID para editar
            val idVal = txtId.text.toString()
            editTxtProductId!!.setText(idVal)
        }
    }

    fun createProductTableRow(product: Product): TableRow {
        val tableRow = TableRow(this)

        // Obtener precio con formato MXN: Redondear precio a dos decimales y obtenerlo como string
        val formattedPrice = formatFloatAsCurrency(product.price, "MXN")

        // Crear elementos de la fila
        val txtId = createTextView(product.id.toString())
        val txtName = createTextView(product.name)
        val txtPrice = createTextView(formattedPrice)
        val txtQuantity = createTextView(product.quantity.toString())

        // Formato para los elementos de la fila
        addFormatToProductRow(txtId, txtName, txtPrice, txtQuantity, false)

        tableRow.addView(txtId)
        tableRow.addView(txtName)
        tableRow.addView(txtPrice)
        tableRow.addView(txtQuantity)

        setProductTableRowClickListener(tableRow, txtId)

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
        productosTableLayout = findViewById<TableLayout>(R.id.productosTableLayout)

        val btnAgregar = findViewById<Button>(R.id.btnAgregar)
        val btnActualizar = findViewById<Button>(R.id.btnActualizar)
        val btnBorrar = findViewById<Button>(R.id.btnBorrar)

        editTxtProductId = findViewById<EditText>(R.id.editTxtProductId)

        fillProductsTable(productosTableLayout!!)

        btnAgregar.setOnClickListener{
            Intent(this,Agregar::class.java).also{
                startActivityForResult(it, 0)
            }
        }

        btnActualizar.setOnClickListener {
            Intent(this,Actualizar::class.java).also {
                val productIdString = editTxtProductId!!.text.toString()
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
            val productIdString = editTxtProductId!!.text.toString()
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

                        // Refresh table
                        fillProductsTable(productosTableLayout!!)
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
                // Refresh table
                fillProductsTable(productosTableLayout!!)
            }
        }
    }
}