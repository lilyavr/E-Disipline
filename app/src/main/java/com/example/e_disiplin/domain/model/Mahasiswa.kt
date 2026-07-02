package com.example.e_disiplin.domain.model

data class Mahasiswa(
    val nim: String = "",
    val name: String = "",
    val email: String = "",
    val noHp: String = "",
    val major: String = "",
    val semester: String = "",
    val tanggalLahir: String = "",
    val password: String = "",
    val totalPoin: Int = 0
)
