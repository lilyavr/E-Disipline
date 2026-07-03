package com.example.e_disiplin.domain.model

/**
 * Represents a specific violation incident committed by a student.
 * Instances start as "Pending" (e.g. scanned via QR) and must be "Verified" by an admin.
 *
 * @property id The unique document ID from Firestore.
 * @property nimMahasiswa The NIM of the student who committed the violation.
 * @property kategoriId The ID of the violation category (empty if pending).
 * @property kategoriName The name of the violation category.
 * @property tingkat The severity level of the violation (e.g., "Ringan").
 * @property poin The penalty points incurred.
 * @property status The state of the record ("Pending" or "Verified").
 * @property tanggal The timestamp of when the violation was recorded.
 */
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
