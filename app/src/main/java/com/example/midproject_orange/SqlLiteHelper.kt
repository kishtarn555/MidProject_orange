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


    fun insert(name:String, price:Float, quanity:Int) {
        val data = ContentValues()
        data.put("name", name)
        data.put("price", price)
        data.put("quantity", quanity)
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

    fun deleteProduct(id:Int) : Boolean{
        return false;
    }
    fun updateProduct(id:Int, name:String, price:Float, quanity: Int): Boolean {
        return false;
    }

    //Returns if an ID exists in the DB
    fun checkId(id:Int): Boolean {
        return false;
    }

}