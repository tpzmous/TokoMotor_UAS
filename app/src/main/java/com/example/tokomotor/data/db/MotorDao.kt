package com.example.tokomotor.data.db

// Import library SQLite Android
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.tokomotor.data.model.Motor
import com.example.tokomotor.util.Constants

/**
 * MotorDao (Data Access Object)
 * - Berfungsi untuk mengelola data Motor di database SQLite
 * - Mendukung operasi CRUD (Create, Read, Update, Delete)
 */
class MotorDao(context: Context) {
    private val dbHelper = MotorDatabase(context) // Helper untuk koneksi database

    /**
     * INSERT DATA (CREATE)
     * - Menyimpan data motor baru ke database
     * - Return: ID dari data yang baru ditambahkan
     */
    fun insert(motor: Motor): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("name", motor.name)
            put("price", motor.price)
            put("imageUrl", motor.imageUrl)
            put("description", motor.description)
        }
        val id = db.insert(Constants.TABLE_MOTOR, null, values) // simpan data
        db.close()
        return id
    }

    /**
     * UPDATE DATA
     * - Mengupdate data motor berdasarkan ID
     * - Return: jumlah row yang berhasil diupdate
     */
    fun update(motor: Motor): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("name", motor.name)
            put("price", motor.price)
            put("imageUrl", motor.imageUrl)
            put("description", motor.description)
        }
        val rows = db.update(
            Constants.TABLE_MOTOR,
            values,
            "id=?",
            arrayOf(motor.id.toString())
        )
        db.close()
        return rows
    }

    /**
     * DELETE DATA
     * - Menghapus data motor berdasarkan ID
     * - Return: jumlah row yang berhasil dihapus
     */
    fun delete(id: Int): Int {
        val db = dbHelper.writableDatabase
        val rows = db.delete(Constants.TABLE_MOTOR, "id=?", arrayOf(id.toString()))
        db.close()
        return rows
    }

    /**
     * GET ALL DATA (READ)
     * - Mengambil semua data motor dari database
     * - Return: List<Motor>
     */
    fun getAll(): List<Motor> {
        val db = dbHelper.readableDatabase
        val list = mutableListOf<Motor>()

        // Query untuk ambil semua data, urut berdasarkan ID DESC
        val cursor: Cursor = db.query(
            Constants.TABLE_MOTOR,
            null,
            null,
            null,
            null,
            null,
            "id DESC"
        )

        // Loop cursor untuk isi list
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"))
                val imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("imageUrl")) ?: ""
                val description = cursor.getString(cursor.getColumnIndexOrThrow("description")) ?: ""
                list.add(Motor(id, name, price, imageUrl, description))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return list
    }

    /**
     * GET BY ID
     * - Mengambil data motor berdasarkan ID tertentu
     * - Return: Motor? (null jika tidak ditemukan)
     */
    fun getById(id: Int): Motor? {
        val db = dbHelper.readableDatabase
        var motor: Motor? = null

        val cursor: Cursor = db.query(
            Constants.TABLE_MOTOR,
            null,
            "id=?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"))
            val imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("imageUrl")) ?: ""
            val description = cursor.getString(cursor.getColumnIndexOrThrow("description")) ?: ""
            motor = Motor(id, name, price, imageUrl, description)
        }

        cursor.close()
        db.close()
        return motor
    }
}
