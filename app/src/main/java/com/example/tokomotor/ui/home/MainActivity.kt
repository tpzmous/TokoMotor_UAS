package com.example.tokomotor.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokomotor.R
import com.example.tokomotor.adapter.MotorAdapter
import com.example.tokomotor.data.repository.MotorRepository
import com.example.tokomotor.ui.auth.LoginActivity
import com.example.tokomotor.util.SessionManager
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var repo: MotorRepository       // repository → akses database motor
    private lateinit var adapter: MotorAdapter       // adapter → tampilkan data di RecyclerView
    private lateinit var session: SessionManager     // session → cek login/logout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 🔹 Set toolbar di layout sebagai ActionBar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        session = SessionManager(this)

        // 🔹 Kalau belum login → redirect ke LoginActivity
        if (!session.isLoggedIn()) {
            val i = Intent(this, LoginActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
            finish()
            return
        }

        repo = MotorRepository(this)

        val rv = findViewById<RecyclerView>(R.id.rvMotors)
        val fab = findViewById<FloatingActionButton>(R.id.fabAdd)

        // 🔹 Adapter dengan 3 aksi (Edit, Delete, Detail)
        adapter = MotorAdapter(
            onEdit = { motor ->
                // Pindah ke AddEditMotorActivity bawa id motor
                val i = Intent(this, AddEditMotorActivity::class.java)
                i.putExtra("id", motor.id)
                startActivity(i)
            },
            onDelete = { motor ->
                // Konfirmasi sebelum hapus
                AlertDialog.Builder(this)
                    .setTitle("Hapus Motor")
                    .setMessage("Yakin mau hapus ${motor.name}?")
                    .setPositiveButton("Ya") { _, _ ->
                        val rows = repo.delete(motor.id)
                        if (rows > 0) {
                            Toast.makeText(this, "Dihapus", Toast.LENGTH_SHORT).show()
                            adapter.removeItem(motor) // update list setelah delete
                        }
                    }
                    .setNegativeButton("Batal", null)
                    .show()
            },
            onDetail = { motor ->
                // Pindah ke halaman detail
                val i = Intent(this, MotorDetailActivity::class.java)
                i.putExtra("id", motor.id)
                startActivity(i)
            }
        )

        // 🔹 Set RecyclerView
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        // 🔹 Tombol tambah motor
        fab.setOnClickListener {
            startActivity(Intent(this, AddEditMotorActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        loadData() // refresh data setiap balik ke halaman utama
    }

    // 🔹 Ambil semua data motor dari repo → tampilkan ke adapter
    private fun loadData() {
        val data = repo.getAll()
        adapter.submitList(data)
    }

    // 🔹 Inflate menu (logout ada di sini)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // 🔹 Aksi ketika menu dipilih
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                // Logout user
                session.logout()
                Toast.makeText(this, "Logout berhasil", Toast.LENGTH_SHORT).show()

                // Arahkan kembali ke login & clear semua activity
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
