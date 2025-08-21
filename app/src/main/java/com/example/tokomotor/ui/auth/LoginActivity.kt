package com.example.tokomotor.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tokomotor.R
import com.example.tokomotor.ui.home.MainActivity
import com.example.tokomotor.util.SessionManager

@Suppress("DEPRECATION")
class LoginActivity: AppCompatActivity() {
    // SessionManager untuk cek status login user
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inisialisasi SessionManager
        session = SessionManager(this)

        // Jika user sudah login sebelumnya → langsung masuk ke MainActivity
        if (session.isLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish() // tutup LoginActivity biar nggak bisa balik pakai back
            return
        }

        // Inisialisasi view dari layout
        val etUser = findViewById<EditText>(R.id.etUsername)
        val etPass = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvToRegister = findViewById<TextView>(R.id.tvToRegister)

        // Aksi saat tombol login ditekan
        btnLogin.setOnClickListener {
            val u = etUser.text.toString().trim()
            val p = etPass.text.toString().trim()

            // Validasi login via SessionManager
            if (session.isValidLogin(u, p)) {
                // Simpan status login = true
                session.setLoggedIn(true)

                // Pindah ke MainActivity dengan animasi
                startActivity(Intent(this, MainActivity::class.java))
                overridePendingTransition(R.anim.fade_in, R.anim.slide_in_right)
                finish()
            } else {
                // Jika username/password salah → tampilkan toast
                Toast.makeText(this, "Username/password salah", Toast.LENGTH_SHORT).show()
            }
        }

        // Aksi pindah ke halaman Register
        tvToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            overridePendingTransition(R.anim.fade_in, R.anim.slide_in_right)
        }
    }
}
