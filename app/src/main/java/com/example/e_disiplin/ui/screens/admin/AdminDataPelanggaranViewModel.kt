package com.example.e_disiplin.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_disiplin.data.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdminDataPelanggaranViewModel : ViewModel() {
    private val repository = FirebaseRepository()

    private val _allViolations = MutableStateFlow<List<ViolationData>>(emptyList())
    val allViolations: StateFlow<List<ViolationData>> = _allViolations

    init {
        fetchViolations()
    }

    private fun fetchViolations() {
        viewModelScope.launch {
            repository.getAllPelanggaranFlow()
                .catch { e -> e.printStackTrace() }
                .collect { list ->
                    val formatter = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
                    val violationDataList = list.sortedByDescending { it.tanggal }.map { p ->
                        val m = repository.getMahasiswa(p.nimMahasiswa)
                        val name = m?.name ?: "Mahasiswa Tidak Ditemukan"
                        val desc = if (p.kategoriName.isNotEmpty()) p.kategoriName else "Pelanggaran Umum"
                        ViolationData(
                            name = name,
                            nim = p.nimMahasiswa,
                            description = desc,
                            date = formatter.format(Date(p.tanggal)),
                            status = p.status,
                            points = "+${p.poin} pts",
                            timestamp = p.tanggal
                        )
                    }
                    _allViolations.value = violationDataList
                }
        }
    }
}
