package com.example.tokomotor.ui.auth

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tokomotor.R
import com.example.tokomotor.util.SessionManager

class RegisterActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // SessionManager untuk simpan data user (username & password)
        val session = SessionManager(this)

        // Inisialisasi view
        val etUser = findViewById<EditText>(R.id.etRegUsername)
        val etPass = findViewById<EditText>(R.id.etRegPassword)
        val etConf = findViewById<EditText>(R.id.etRegConfirm)
        val btn = findViewById<Button>(R.id.btnRegister)

        // Aksi tombol Register
        btn.setOnClickListener {
            val u = etUser.text.toString().trim()
            val p = etPass.text.toString().trim()
            val c = etConf.text.toString().trim()

            // Validasi input kosong
            if (u.isEmpty() || p.isEmpty() || c.isEmpty()) {
                Toast.makeText(this, "Lengkapi semua field", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validasi password & konfirmasi password
            if (p != c) {
                Toast.makeText(this, "Konfirmasi password tidak cocok", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Simpan user baru ke SessionManager
            session.saveUser(u, p)

            // Beri feedback sukses
            Toast.makeText(this, "Registrasi berhasil, silakan login", Toast.LENGTH_SHORT).show()

            // Tutup RegisterActivity → kembali ke LoginActivity
            finish()
        }
    }
}
