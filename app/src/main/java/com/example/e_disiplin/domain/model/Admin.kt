package com.example.e_disiplin.domain.model

/**
 * Represents an administrator account in the system.
 * Used for authentication and profile management in the Admin portal.
 *
 * @property id The unique document ID from Firestore.
 * @property name The full name of the administrator.
 * @property email The registered email address.
 * @property username The username used for login.
 * @property password The password used for login.
 */
data class Admin(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val username: String = "",
    val password: String = ""
)
