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
            KategoriPelanggaran(nama = "Berpakaian tidak sesuai kode etik", tingkat = "ringan", poin = 2),
            KategoriPelanggaran(nama = "Tidak memakai jas almamater pada kegiatan wajib", tingkat = "ringan", poin = 2),
            KategoriPelanggaran(nama = "Membuang sampah sembarangan", tingkat = "ringan", poin = 3),
            KategoriPelanggaran(nama = "Mengganggu jalan perkuliahan", tingkat = "sedang", poin = 5),
            KategoriPelanggaran(nama = "Menggunakan HP saat kuliah tanpa izin", tingkat = "sedang", poin = 4),
            KategoriPelanggaran(nama = "Merokok atau membawa Vape di area kampus", tingkat = "berat", poin = 7),
            KategoriPelanggaran(nama = "Plagiarisme", tingkat = "berat", poin = 10),
            KategoriPelanggaran(nama = "Merusak fasilitas kampus", tingkat = "berat", poin = 8)
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
