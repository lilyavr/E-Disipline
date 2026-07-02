package com.example.e_disiplin.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_disiplin.data.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MahasiswaLoginViewModel : ViewModel() {
    private val repository = FirebaseRepository()

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(nim: String, password: String) {
        if (nim.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("NIM and Password cannot be empty")
            return
        }

        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            // Hash the entered password before comparing with the stored SHA-256 hash
            val hashedPassword = hashSha256(password)
            val success = repository.loginMahasiswa(nim, hashedPassword)
            if (success) {
                _loginState.value = LoginState.Success
            } else {
                _loginState.value = LoginState.Error("Invalid NIM or password")
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
        _loginState.value = LoginState.Idle
    }
}
