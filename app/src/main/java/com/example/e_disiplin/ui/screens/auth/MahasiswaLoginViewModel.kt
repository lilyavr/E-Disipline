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
            val success = repository.loginMahasiswa(nim, password)
            if (success) {
                _loginState.value = LoginState.Success
            } else {
                _loginState.value = LoginState.Error("Invalid NIM or password")
            }
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}
