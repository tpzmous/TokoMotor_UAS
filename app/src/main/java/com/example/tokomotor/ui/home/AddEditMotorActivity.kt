package com.example.tokomotor.ui.home

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tokomotor.R
import com.example.tokomotor.data.model.Motor
import com.example.tokomotor.data.repository.MotorRepository

class AddEditMotorActivity: AppCompatActivity() {

    // Repository untuk CRUD motor
    private lateinit var repo: MotorRepository
    private var editId: Int? = null   // ID motor kalau sedang edit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_motor)

        repo = MotorRepository(this)

        // Inisialisasi view
        val tvTitle = findViewById<TextView>(R.id.tvFormTitle)
        val etName = findViewById<EditText>(R.id.etName)
        val etDescription = findViewById<EditText>(R.id.etDescription)
        val etPrice = findViewById<EditText>(R.id.etPrice)
        val etImage = findViewById<EditText>(R.id.etImageUrl)
        val btnSave = findViewById<Button>(R.id.btnSave)

        // Cek apakah activity dipanggil untuk Edit (ada id di intent)
        editId = intent.getIntExtra("id", -1).takeIf { it != -1 }

        if (editId != null) {
            // Mode Edit
            tvTitle.text = "Edit Motor"
            val m = repo.getById(editId!!) ?: return
            etName.setText(m.name)
            etDescription.setText(m.description)
            etPrice.setText(m.price.toString())
            etImage.setText(m.imageUrl)
            btnSave.text = getString(R.string.update)
        }

        // Aksi tombol simpan
        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val description = etDescription.text.toString().trim()
            val priceStr = etPrice.text.toString().trim()
            val image = etImage.text.toString().trim()

            // Validasi input wajib
            if (name.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "Nama & harga wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validasi harga harus angka
            val price = priceStr.toDoubleOrNull()
            if (price == null) {
                Toast.makeText(this, "Harga tidak valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (editId == null) {
                // Mode Tambah → insert ke DB
                repo.insert(
                    Motor(name = name, price = price, imageUrl = image, description = description)
                )
                Toast.makeText(this, "Tersimpan", Toast.LENGTH_SHORT).show()
            } else {
                // Mode Edit → update data di DB
                repo.update(
                    Motor(id = editId!!, name = name, price = price, imageUrl = image, description = description)
                )
                Toast.makeText(this, "Diperbarui", Toast.LENGTH_SHORT).show()
            }

            // Tutup activity dan kembali ke list
            finish()
        }
    }
}
