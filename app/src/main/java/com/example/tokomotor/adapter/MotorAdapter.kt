package com.example.tokomotor.adapter

// Import library yang diperlukan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tokomotor.R
import com.example.tokomotor.data.model.Motor
import java.text.NumberFormat
import java.util.*

/**
 * Adapter untuk menampilkan daftar Motor di RecyclerView
 * - Menggunakan ListAdapter agar lebih efisien (membandingkan data dengan DiffUtil)
 * - Mendukung fitur edit, delete, dan detail
 */
class MotorAdapter(
    private val onEdit: (Motor) -> Unit,   // callback untuk tombol Edit
    private val onDelete: (Motor) -> Unit, // callback untuk tombol Delete
    private val onDetail: (Motor) -> Unit  // callback untuk klik item (detail)
) : ListAdapter<Motor, MotorAdapter.MotorVH>(DiffCallback) {

    /**
     * ViewHolder: wadah untuk setiap item motor di dalam RecyclerView
     */
    class MotorVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.imgMotor) // gambar motor
        val name: TextView = itemView.findViewById(R.id.tvName)   // nama motor
        val price: TextView = itemView.findViewById(R.id.tvPrice) // harga motor
        val btnEdit: Button = itemView.findViewById(R.id.btnEdit) // tombol edit
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete) // tombol hapus
    }

    /**
     * Membuat ViewHolder baru ketika RecyclerView membutuhkan item baru
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MotorVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_motor, parent, false)
        return MotorVH(view)
    }

    /**
     * Menghubungkan data Motor dengan ViewHolder
     */
    override fun onBindViewHolder(holder: MotorVH, position: Int) {
        val item = getItem(position) // ambil data motor berdasarkan posisi
        holder.name.text = item.name

        // Format harga dengan locale Indonesia (contoh: Rp 15.000.000)
        val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        formatter.maximumFractionDigits = 0
        holder.price.text = formatter.format(item.price)

        // Load gambar motor dengan Glide (support URL / local file)
        Glide.with(holder.itemView)
            .load(item.imageUrl)
            .placeholder(R.mipmap.ic_launcher) // jika gambar belum ada
            .error(R.mipmap.ic_launcher)       // jika gagal load gambar
            .into(holder.img)

        // Aksi tombol dan klik item
        holder.btnEdit.setOnClickListener { onEdit(item) }     // edit data
        holder.btnDelete.setOnClickListener { onDelete(item) } // hapus data
        holder.itemView.setOnClickListener { onDetail(item) }  // buka detail
    }

    /**
     * Fungsi tambahan: hapus item dari list adapter
     */
    fun removeItem(motor: Motor) {
        val currentList = currentList.toMutableList()
        val index = currentList.indexOfFirst { it.id == motor.id }
        if (index != -1) {
            currentList.removeAt(index)
            submitList(currentList) // update list di RecyclerView
        }
    }

    /**
     * DiffUtil: membandingkan item lama dan baru
     * - areItemsTheSame: cek apakah item punya ID yang sama
     * - areContentsTheSame: cek apakah isi data sama persis
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Motor>() {
        override fun areItemsTheSame(oldItem: Motor, newItem: Motor): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Motor, newItem: Motor): Boolean =
            oldItem == newItem
    }
}
