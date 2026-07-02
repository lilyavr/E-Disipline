package com.example.e_disiplin.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_disiplin.data.repository.FirebaseRepository
import com.example.e_disiplin.domain.model.Mahasiswa
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlinx.coroutines.flow.catch

sealed class AddMahasiswaState {
    object Idle : AddMahasiswaState()
    object Loading : AddMahasiswaState()
    object Success : AddMahasiswaState()
    data class Error(val message: String) : AddMahasiswaState()
}

class AdminMahasiswaViewModel : ViewModel() {
    private val repository = FirebaseRepository()

    private val _totalMahasiswa = MutableStateFlow(0L)
    val totalMahasiswa: StateFlow<Long> = _totalMahasiswa

    private val _addState = MutableStateFlow<AddMahasiswaState>(AddMahasiswaState.Idle)
    val addState: StateFlow<AddMahasiswaState> = _addState

    private val _pendingCount = MutableStateFlow(0)
    val pendingCount: StateFlow<Int> = _pendingCount

    private val _todayCount = MutableStateFlow(0)
    val todayCount: StateFlow<Int> = _todayCount

    private val _verifiedMonthCount = MutableStateFlow(0)
    val verifiedMonthCount: StateFlow<Int> = _verifiedMonthCount

    init {
        fetchTotalMahasiswa()
        observePelanggaranStats()
    }

    private fun observePelanggaranStats() {
        viewModelScope.launch {
            repository.getAllPelanggaranFlow()
                .catch { e -> e.printStackTrace() }
                .collect { list ->
                    // Calculate Pending
                    _pendingCount.value = list.count { it.status == "Pending" }

                    val now = Calendar.getInstance()
                    val currentYear = now.get(Calendar.YEAR)
                    val currentMonth = now.get(Calendar.MONTH)
                    val currentDay = now.get(Calendar.DAY_OF_MONTH)

                    var today = 0
                    var verifiedMonth = 0

                    for (p in list) {
                        val cal = Calendar.getInstance().apply { timeInMillis = p.tanggal }
                        val pYear = cal.get(Calendar.YEAR)
                        val pMonth = cal.get(Calendar.MONTH)
                        val pDay = cal.get(Calendar.DAY_OF_MONTH)

                        if (pYear == currentYear && pMonth == currentMonth && pDay == currentDay) {
                            today++
                        }

                        if (p.status == "Verified" && pYear == currentYear && pMonth == currentMonth) {
                            verifiedMonth++
                        }
                    }

                    _todayCount.value = today
                    _verifiedMonthCount.value = verifiedMonth
                }
        }
    }

    fun fetchTotalMahasiswa() {
        viewModelScope.launch {
            _totalMahasiswa.value = repository.getTotalMahasiswaCount()
        }
    }

    fun addMahasiswa(nim: String, name: String, email: String, noHp: String, major: String, semester: String, tanggalLahir: String) {
        if (nim.isBlank() || name.isBlank() || email.isBlank() || noHp.isBlank() || major.isBlank() || semester.isBlank() || tanggalLahir.isBlank()) {
            _addState.value = AddMahasiswaState.Error("Semua field harus diisi")
            return
        }

        _addState.value = AddMahasiswaState.Loading
        viewModelScope.launch {
            val password = tanggalLahir
            
            val mahasiswa = Mahasiswa(
                nim = nim, 
                name = name, 
                email = email, 
                noHp = noHp, 
                major = major, 
                semester = semester,
                tanggalLahir = tanggalLahir,
                password = password
            )
            val success = repository.addMahasiswa(mahasiswa)
            if (success) {
                _addState.value = AddMahasiswaState.Success
                fetchTotalMahasiswa() // refresh count
            } else {
                _addState.value = AddMahasiswaState.Error("Gagal menyimpan data")
            }
        }
    }

    fun resetAddState() {
        _addState.value = AddMahasiswaState.Idle
    }
}
