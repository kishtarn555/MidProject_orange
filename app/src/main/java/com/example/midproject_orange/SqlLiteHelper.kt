package com.example.sqliteproject

import android.content.ContentValues
import  android.content.Context
import android.database.sqlite.SQLiteDatabase
import  android.database.sqlite.SQLiteOpenHelper
import com.example.midproject_orange.Product

class SQLiteHelper(context:Context) :  SQLiteOpenHelper(context, "agenda_info", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val script = "CREATE TABLE products"+
                "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT,"+
                "price DECIMAL,"+
                "quantity INT)"
        db!!.execSQL(script)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val script = "DROP TABLE IF EXISTS products"
        db!!.execSQL(script)
        onCreate(db)
    }


    fun insert(name:String, price:Float, quantity:Int) {
        val data = ContentValues()
        data.put("name", name)
        data.put("price", price)
        data.put("quantity", quantity)
        val db = this.writableDatabase
        db.insert("products",null, data)
        db.close()
    }

    fun getAllProducts(): List<Product> {
        val productList = mutableListOf<Product>()
        val query = "SELECT * FROM products"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val price = cursor.getFloat(cursor.getColumnIndexOrThrow("price"))
                val quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"))
                val product = Product(id, name, price, quantity)
                productList.add(product)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return productList
    }

    //Returns true if the Id existed and was deleted
    fun deleteProduct(id: Int): Boolean {
        val db = this.writableDatabase
        val rowsAffected = db.delete("products", "id=?", arrayOf(id.toString()))

        db.close()

        return rowsAffected > 0
    }
    fun updateProduct(id:Int, name:String, price:Float, quantity: Int): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("name", name)
        values.put("price", price)
        values.put("quantity", quantity)

        val rowsAffected = db.update("products", values, "id=?",  arrayOf(id.toString()))
        db.close()
        return rowsAffected > 0
    }

    //Returns true if an ID exists in the DB
    fun checkId(id: Int): Boolean {
        val db = this.readableDatabase
        val query = "SELECT id FROM products WHERE id = ?"
        val cursor = db.rawQuery(query, arrayOf(id.toString()))

        val idExists = cursor.moveToFirst()

        cursor.close()
        db.close()

        return idExists
    }


}