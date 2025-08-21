package com.example.tokomotor.data.model

data class Motor(
    val id: Int = 0,
    val name: String,
    val price: Double,
    val imageUrl: String = "",
    val description: String = "" // ✅ kolom baru
)
