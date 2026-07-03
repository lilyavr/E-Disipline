package com.example.e_disiplin.domain.model

/**
 * Represents a predefined category/type of disciplinary violation.
 * Defines the standard penalty points for a given offense.
 *
 * @property id The unique document ID from Firestore.
 * @property nama The descriptive name of the violation category.
 * @property tingkat The severity level (e.g., "ringan", "sedang", "berat").
 * @property poin The standard points applied for this violation.
 */
data class KategoriPelanggaran(
    val id: String = "",
    val nama: String = "",
    val tingkat: String = "", // "ringan", "sedang", "berat"
    val poin: Int = 0
)
