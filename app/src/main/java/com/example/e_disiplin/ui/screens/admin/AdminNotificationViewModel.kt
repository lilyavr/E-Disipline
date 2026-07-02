package com.example.e_disiplin.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_disiplin.data.repository.FirebaseRepository
import com.example.e_disiplin.domain.model.KategoriPelanggaran
import com.example.e_disiplin.domain.model.Mahasiswa
import com.example.e_disiplin.domain.model.Pelanggaran
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AdminNotificationViewModel : ViewModel() {
    private val repository = FirebaseRepository()

    private val _pendingPelanggaran = MutableStateFlow<List<Pelanggaran>>(emptyList())
    val pendingPelanggaran: StateFlow<List<Pelanggaran>> = _pendingPelanggaran

    private val _kategoriList = MutableStateFlow<List<KategoriPelanggaran>>(emptyList())
    val kategoriList: StateFlow<List<KategoriPelanggaran>> = _kategoriList

    private val _mahasiswaMap = MutableStateFlow<Map<String, Mahasiswa>>(emptyMap())
    val mahasiswaMap: StateFlow<Map<String, Mahasiswa>> = _mahasiswaMap

    init {
        fetchPendingPelanggaran()
        fetchKategoriList()
    }

    private fun fetchPendingPelanggaran() {
        viewModelScope.launch {
            repository.getPendingPelanggaranFlow()
                .catch { e ->
                    e.printStackTrace()
                }
                .collect { list ->
                    _pendingPelanggaran.value = list
                    // Fetch Mahasiswa info for these nim
                    val map = _mahasiswaMap.value.toMutableMap()
                    list.forEach { p ->
                        if (!map.containsKey(p.nimMahasiswa)) {
                            val m = repository.getMahasiswa(p.nimMahasiswa)
                            if (m != null) {
                                map[p.nimMahasiswa] = m
                            }
                        }
                    }
                    _mahasiswaMap.value = map
                }
        }
    }

    private fun fetchKategoriList() {
        viewModelScope.launch {
            _kategoriList.value = repository.getKategoriList()
        }
    }

    fun verifyPelanggaran(
        pelanggaranId: String,
        nimMahasiswa: String,
        kategoriId: String,
        kategoriName: String,
        tingkat: String,
        poin: Int,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            val success = repository.verifyPelanggaran(
                pelanggaranId = pelanggaranId,
                nimMahasiswa = nimMahasiswa,
                kategoriId = kategoriId,
                kategoriName = kategoriName,
                tingkat = tingkat,
                poin = poin
            )
            if (success) {
                onSuccess()
            } else {
                onError()
            }
        }
    }
}
