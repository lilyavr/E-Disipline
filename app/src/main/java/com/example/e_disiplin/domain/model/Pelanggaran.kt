package com.example.e_disiplin.domain.model

data class Pelanggaran(
    val id: String = "",
    val nimMahasiswa: String = "",
    val kategoriId: String = "",
    val kategoriName: String = "",
    val tingkat: String = "",
    val poin: Int = 0,
    val status: String = "Pending", // Pending, Verified
    val tanggal: Long = System.currentTimeMillis()
)
