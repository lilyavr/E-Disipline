package com.example.e_disiplin.domain.model

data class KategoriPelanggaran(
    val id: String = "",
    val nama: String = "",
    val tingkat: String = "", // "ringan", "sedang", "berat"
    val poin: Int = 0
)
