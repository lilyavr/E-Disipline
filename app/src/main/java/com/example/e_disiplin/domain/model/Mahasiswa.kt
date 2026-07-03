package com.example.e_disiplin.domain.model

/**
 * Represents a student profile in the system.
 * Contains both personal details and their disciplinary standing (points).
 *
 * @property nim The unique Student Identification Number (NIM), used as the document ID.
 * @property name The full name of the student.
 * @property email The registered email address.
 * @property noHp The student's phone number.
 * @property major The academic department/major (Jurusan).
 * @property semester The current semester level.
 * @property tanggalLahir Date of Birth (format: DD-MM-YYYY), used for password reset verification.
 * @property password The hashed or raw password used for login.
 * @property totalPoin The total accumulated penalty points for the student.
 */
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
