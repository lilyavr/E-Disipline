package com.example.e_disiplin.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_disiplin.data.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val adminId: String) : LoginState()
    data class Error(val message: String) : LoginState()
}

/**
 * ViewModel managing the state and logic for the Admin Login screen.
 * It interfaces with the [FirebaseRepository] to authenticate admin credentials.
 */
class AdminLoginViewModel : ViewModel() {
    private val repository = FirebaseRepository()

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    /**
     * Attempts to log in the admin using the provided credentials.
     * Updates [loginState] based on the authentication result.
     *
     * @param username The entered username.
     * @param password The entered raw password.
     */
    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("Username and Password cannot be empty")
            return
        }

        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            val admin = repository.loginAdmin(username, password)
            if (admin != null) {
                _loginState.value = LoginState.Success(admin.id)
            } else {
                _loginState.value = LoginState.Error("Invalid username or password")
            }
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}
