package com.example.e_disiplin.ui.screens.admin

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_disiplin.data.repository.FirebaseRepository
import com.example.e_disiplin.domain.model.Admin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class KelolaAkunState {
    object Idle : KelolaAkunState()
    object Loading : KelolaAkunState()
    object Success : KelolaAkunState()
    data class Error(val message: String) : KelolaAkunState()
}

/**
 * ViewModel responsible for managing the Admin's own profile and account settings.
 * It retrieves the active session ID from SharedPreferences and fetches/updates
 * the admin's profile data via [FirebaseRepository].
 */
class AdminKelolaAkunViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FirebaseRepository()
    private val sharedPrefs = application.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)

    private val _adminData = MutableStateFlow<Admin?>(null)
    val adminData: StateFlow<Admin?> = _adminData

    private val _updateState = MutableStateFlow<KelolaAkunState>(KelolaAkunState.Idle)
    val updateState: StateFlow<KelolaAkunState> = _updateState

    init {
        loadAdminData()
    }

    private fun loadAdminData() {
        val adminId = sharedPrefs.getString("adminId", null)
        if (adminId != null) {
            viewModelScope.launch {
                val admin = repository.getAdmin(adminId)
                _adminData.value = admin
            }
        }
    }

    fun updateAdminData(name: String, email: String, username: String, pass: String) {
        val currentAdmin = _adminData.value
        if (currentAdmin == null) {
            _updateState.value = KelolaAkunState.Error("Admin data not found")
            return
        }

        if (name.isBlank() || email.isBlank() || username.isBlank() || pass.isBlank()) {
            _updateState.value = KelolaAkunState.Error("Semua field harus diisi")
            return
        }

        _updateState.value = KelolaAkunState.Loading

        viewModelScope.launch {
            val updatedAdmin = currentAdmin.copy(
                name = name,
                email = email,
                username = username,
                password = pass
            )
            val success = repository.updateAdmin(updatedAdmin)
            if (success) {
                _adminData.value = updatedAdmin
                _updateState.value = KelolaAkunState.Success
            } else {
                _updateState.value = KelolaAkunState.Error("Gagal memperbarui data")
            }
        }
    }

    fun resetState() {
        _updateState.value = KelolaAkunState.Idle
    }
}
