package com.example.tokomotor.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tokomotor.util.Constants

/**
 * MotorDatabase
 * - Class yang bertugas untuk membuat dan mengelola database SQLite
 * - Menggunakan SQLiteOpenHelper untuk handle database versioning
 */
class MotorDatabase(context: Context) :
    SQLiteOpenHelper(context, Constants.DB_NAME, null, Constants.DB_VERSION) {

    /**
     * onCreate()
     * - Dipanggil saat database pertama kali dibuat
     * - Membuat tabel Motor dengan field:
     *   id (primary key), name (text), price (real), imageUrl (text), description (text)
     */
    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE ${Constants.TABLE_MOTOR} (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                price REAL NOT NULL,
                imageUrl TEXT,
                description TEXT
            );
        """.trimIndent()
        db.execSQL(createTable) // eksekusi query buat bikin tabel
    }

    /**
     * onUpgrade()
     * - Dipanggil saat ada perubahan versi database (DB_VERSION naik)
     * - Bisa digunakan untuk menambah kolom baru atau recreate tabel
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Jika versi lama < 2 → tambahin kolom description
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE ${Constants.TABLE_MOTOR} ADD COLUMN description TEXT")
        }

        // Jika versi lama < 3 → drop table lama, lalu buat ulang tabel
        if (oldVersion < 3) {
            db.execSQL("DROP TABLE IF EXISTS ${Constants.TABLE_MOTOR}")
            onCreate(db)
        }
    }
}
