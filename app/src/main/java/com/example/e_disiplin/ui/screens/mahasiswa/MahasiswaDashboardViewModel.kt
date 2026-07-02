package com.example.e_disiplin.ui.screens.mahasiswa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_disiplin.data.repository.FirebaseRepository
import com.example.e_disiplin.domain.model.Mahasiswa
import com.example.e_disiplin.domain.model.Pelanggaran
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MahasiswaDashboardViewModel : ViewModel() {
    private val repository = FirebaseRepository()

    private val _mahasiswa = MutableStateFlow<Mahasiswa?>(null)
    val mahasiswa: StateFlow<Mahasiswa?> = _mahasiswa

    private val _lastScannedPelanggaran = MutableStateFlow<Pelanggaran?>(null)
    val lastScannedPelanggaran: StateFlow<Pelanggaran?> = _lastScannedPelanggaran

    // Live total poin from Firebase
    private val _totalPoin = MutableStateFlow(0)
    val totalPoin: StateFlow<Int> = _totalPoin

    // Verified violations for notification feed
    private val _verifiedPelanggaran = MutableStateFlow<List<Pelanggaran>>(emptyList())
    val verifiedPelanggaran: StateFlow<List<Pelanggaran>> = _verifiedPelanggaran

    // All violations for this student
    private val _allPelanggaran = MutableStateFlow<List<Pelanggaran>>(emptyList())
    val allPelanggaran: StateFlow<List<Pelanggaran>> = _allPelanggaran

    fun fetchMahasiswa(nim: String) {
        if (nim.isEmpty()) return
        viewModelScope.launch {
            _mahasiswa.value = repository.getMahasiswa(nim)
        }
        // Observe real-time violations for this NIM
        viewModelScope.launch {
            repository.getPelanggaranByNimFlow(nim)
                .catch { e -> e.printStackTrace() }
                .collect { list ->
                    _allPelanggaran.value = list
                    _verifiedPelanggaran.value = list.filter { it.status == "Verified" }
                    _totalPoin.value = list.filter { it.status == "Verified" }.sumOf { it.poin }
                }
        }
    }

    fun processScan(qrContent: String, nimMahasiswa: String, onSuccess: (Pelanggaran) -> Unit) {
        viewModelScope.launch {
            val pendingPelanggaran = repository.addPendingPelanggaran(nimMahasiswa, qrContent)
            if (pendingPelanggaran != null) {
                _lastScannedPelanggaran.value = pendingPelanggaran
                onSuccess(pendingPelanggaran)
            }
        }
    }
}
