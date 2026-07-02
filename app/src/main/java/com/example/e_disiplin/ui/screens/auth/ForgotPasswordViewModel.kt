package com.example.e_disiplin.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_disiplin.data.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ForgotPasswordState {
    object Idle : ForgotPasswordState()
    object Loading : ForgotPasswordState()
    object VerificationSuccess : ForgotPasswordState()
    object ResetSuccess : ForgotPasswordState()
    data class Error(val message: String) : ForgotPasswordState()
}

class ForgotPasswordViewModel : ViewModel() {
    private val repository = FirebaseRepository()

    private val _forgotPasswordState = MutableStateFlow<ForgotPasswordState>(ForgotPasswordState.Idle)
    val forgotPasswordState: StateFlow<ForgotPasswordState> = _forgotPasswordState

    // Mahasiswa Verification
    fun verifyMahasiswa(nim: String, tanggalLahir: String) {
        if (nim.isBlank() || tanggalLahir.isBlank()) {
            _forgotPasswordState.value = ForgotPasswordState.Error("NIM dan Tanggal Lahir tidak boleh kosong")
            return
        }
        
        _forgotPasswordState.value = ForgotPasswordState.Loading
        viewModelScope.launch {
            val success = repository.verifyMahasiswaForReset(nim, tanggalLahir)
            if (success) {
                _forgotPasswordState.value = ForgotPasswordState.VerificationSuccess
            } else {
                _forgotPasswordState.value = ForgotPasswordState.Error("NIM atau Tanggal Lahir tidak cocok dengan data kami")
            }
        }
    }

    // Admin Verification
    fun verifyAdmin(username: String, email: String) {
        if (username.isBlank() || email.isBlank()) {
            _forgotPasswordState.value = ForgotPasswordState.Error("Username dan Email tidak boleh kosong")
            return
        }

        _forgotPasswordState.value = ForgotPasswordState.Loading
        viewModelScope.launch {
            val success = repository.verifyAdminForReset(username, email)
            if (success) {
                _forgotPasswordState.value = ForgotPasswordState.VerificationSuccess
            } else {
                _forgotPasswordState.value = ForgotPasswordState.Error("Username atau Email tidak cocok dengan data kami")
            }
        }
    }

    // Reset Password
    fun resetPassword(identifier: String, newPassword: String, isMahasiswa: Boolean) {
        if (newPassword.isBlank()) {
            _forgotPasswordState.value = ForgotPasswordState.Error("Password baru tidak boleh kosong")
            return
        }

        _forgotPasswordState.value = ForgotPasswordState.Loading
        viewModelScope.launch {
            val hashedPassword = hashSha256(newPassword)
            val success = if (isMahasiswa) {
                repository.resetMahasiswaPassword(identifier, hashedPassword)
            } else {
                repository.resetAdminPassword(identifier, hashedPassword)
            }

            if (success) {
                _forgotPasswordState.value = ForgotPasswordState.ResetSuccess
            } else {
                _forgotPasswordState.value = ForgotPasswordState.Error("Gagal mereset password. Silakan coba lagi.")
            }
        }
    }

    /**
     * Returns the SHA-256 hex digest of [input].
     */
    private fun hashSha256(input: String): String {
        val bytes = java.security.MessageDigest
            .getInstance("SHA-256")
            .digest(input.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun resetState() {
        _forgotPasswordState.value = ForgotPasswordState.Idle
    }
}
