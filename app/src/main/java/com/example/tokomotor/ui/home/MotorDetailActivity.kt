package com.example.tokomotor.ui.home

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.tokomotor.R
import com.example.tokomotor.data.repository.MotorRepository
import java.text.NumberFormat
import java.util.*

class MotorDetailActivity : AppCompatActivity() {
    private lateinit var repo: MotorRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motor_detail)

        repo = MotorRepository(this)

        val ivMotor = findViewById<ImageView>(R.id.ivMotor)
        val tvName = findViewById<TextView>(R.id.tvName)
        val tvPrice = findViewById<TextView>(R.id.tvPrice)
        val tvDescription = findViewById<TextView>(R.id.tvDescription)
        val btnBack = findViewById<Button>(R.id.btnBack)

        // 🔹 Ambil id motor dari intent
        val id = intent.getIntExtra("id", -1)
        if (id != -1) {
            val motor = repo.getById(id) // ambil data motor dari database
            motor?.let {
                // tampilkan nama
                tvName.text = it.name

                // 🔹 Format harga ke Rupiah pakai Locale Indonesia
                val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
                val hargaFormatted = formatter.format(it.price)
                tvPrice.text = hargaFormatted

                // tampilkan deskripsi
                tvDescription.text = it.description

                // 🔹 Load gambar pakai Glide (jika ada url)
                if (it.imageUrl.isNotEmpty()) {
                    Glide.with(this)
                        .load(it.imageUrl)
                        .placeholder(R.drawable.ic_motor_placeholder) // kalau url kosong/error tampilkan placeholder
                        .into(ivMotor)
                }
            }
        }

        // 🔹 Tombol kembali → tutup halaman detail
        btnBack.setOnClickListener { finish() }
    }
}
