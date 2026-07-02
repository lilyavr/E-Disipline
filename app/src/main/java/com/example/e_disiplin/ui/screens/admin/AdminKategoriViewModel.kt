package com.example.e_disiplin.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_disiplin.data.repository.FirebaseRepository
import com.example.e_disiplin.domain.model.KategoriPelanggaran
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminKategoriViewModel : ViewModel() {
    private val repository = FirebaseRepository()

    private val _kategoriList = MutableStateFlow<List<KategoriPelanggaran>>(emptyList())
    val kategoriList: StateFlow<List<KategoriPelanggaran>> = _kategoriList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchKategoriList()
    }

    fun fetchKategoriList() {
        viewModelScope.launch {
            _isLoading.value = true
            val list = repository.getKategoriList()
            if (list.isEmpty()) {
                seedInitialKategori()
            } else {
                _kategoriList.value = list
            }
            _isLoading.value = false
        }
    }

    private suspend fun seedInitialKategori() {
        val defaultCategories = listOf(
            KategoriPelanggaran(nama = "Keterlambatan Hadir", tingkat = "ringan", poin = 5),
            KategoriPelanggaran(nama = "Pelanggaran Pakaian", tingkat = "ringan", poin = 5),
            KategoriPelanggaran(nama = "Penggunaan HP Saat Kuliah", tingkat = "sedang", poin = 10),
            KategoriPelanggaran(nama = "Ketidakhadiran Tanpa Izin", tingkat = "sedang", poin = 15),
            KategoriPelanggaran(nama = "Perkelahian / Kekerasan", tingkat = "berat", poin = 30),
            KategoriPelanggaran(nama = "Pemalsuan Dokumen", tingkat = "berat", poin = 40)
        )
        
        for (kategori in defaultCategories) {
            repository.addKategori(kategori)
        }
        
        // Fetch again after seeding
        _kategoriList.value = repository.getKategoriList()
    }

    fun addKategori(nama: String, tingkat: String, poin: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val success = repository.addKategori(KategoriPelanggaran(nama = nama, tingkat = tingkat, poin = poin))
            if (success) {
                fetchKategoriList()
            }
            _isLoading.value = false
        }
    }

    fun deleteKategori(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val success = repository.deleteKategori(id)
            if (success) {
                fetchKategoriList()
            }
            _isLoading.value = false
        }
    }
}
