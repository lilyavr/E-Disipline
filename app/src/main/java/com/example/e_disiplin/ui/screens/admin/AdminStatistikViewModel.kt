package com.example.e_disiplin.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_disiplin.data.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.Calendar

data class MonthlyChartData(
    val month: String,        // "Jan", "Feb", etc.
    val count: Int
)

data class CategoryChartData(
    val category: String,
    val count: Int,
    val percentage: Float
)

data class JurusanChartData(
    val jurusan: String,
    val count: Int,
    val percentage: Float
)

data class StatistikUiState(
    val isLoading: Boolean = true,
    val selectedYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    val availableYears: List<Int> = emptyList(),
    val totalPelanggaran: Int = 0,
    val monthlyData: List<MonthlyChartData> = emptyList(),
    val categoryData: List<CategoryChartData> = emptyList(),
    val jurusanData: List<JurusanChartData> = emptyList(),
    val isExporting: Boolean = false
)

/**
 * ViewModel responsible for aggregating and processing violation data for statistical analysis.
 * It groups violations by Month, Category, and Jurusan (Major) based on a selected Year,
 * feeding this structured data to the Admin Statistik screen charts.
 */
class AdminStatistikViewModel : ViewModel() {

    private val repository = FirebaseRepository()

    private val _uiState = MutableStateFlow(StatistikUiState())
    val uiState: StateFlow<StatistikUiState> = _uiState

    private val monthNames = listOf(
        "Jan", "Feb", "Mar", "Apr", "Mei", "Jun",
        "Jul", "Agu", "Sep", "Okt", "Nov", "Des"
    )

    init {
        fetchData()
    }

    fun selectYear(year: Int) {
        _uiState.value = _uiState.value.copy(selectedYear = year)
        // Re-process with the stored raw data
        viewModelScope.launch {
            processCurrentData(year)
        }
    }

    private var cachedPelanggaranList: List<com.example.e_disiplin.domain.model.Pelanggaran> = emptyList()
    private var cachedMahasiswaMap: Map<String, String> = emptyMap() // nim -> major

    private fun fetchData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.getAllPelanggaranFlow()
                .catch { e -> e.printStackTrace() }
                .collect { pelanggaran ->
                    cachedPelanggaranList = pelanggaran

                    // Collect unique NIMs and fetch their majors
                    val nims = pelanggaran.map { it.nimMahasiswa }.distinct()
                    val majorMap = mutableMapOf<String, String>()
                    for (nim in nims) {
                        val mhs = repository.getMahasiswa(nim)
                        if (mhs != null) {
                            majorMap[nim] = mhs.major.ifEmpty { "Lainnya" }
                        }
                    }
                    cachedMahasiswaMap = majorMap

                    // Determine available years
                    val years = pelanggaran
                        .map { p ->
                            val cal = Calendar.getInstance()
                            cal.timeInMillis = p.tanggal
                            cal.get(Calendar.YEAR)
                        }
                        .distinct()
                        .sorted()
                        .let { list ->
                            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                            if (currentYear !in list) list + currentYear else list
                        }

                    val selectedYear = _uiState.value.selectedYear
                    val chartYear = if (selectedYear in years) selectedYear else years.lastOrNull() ?: Calendar.getInstance().get(Calendar.YEAR)

                    processData(pelanggaran, majorMap, years, chartYear)
                }
        }
    }

    private suspend fun processCurrentData(year: Int) {
        processData(cachedPelanggaranList, cachedMahasiswaMap, _uiState.value.availableYears, year)
    }

    private fun processData(
        pelanggaran: List<com.example.e_disiplin.domain.model.Pelanggaran>,
        majorMap: Map<String, String>,
        availableYears: List<Int>,
        selectedYear: Int
    ) {
        val filtered = pelanggaran.filter { p ->
            val cal = Calendar.getInstance()
            cal.timeInMillis = p.tanggal
            cal.get(Calendar.YEAR) == selectedYear
        }

        // 1. Monthly data
        val monthlyCounts = IntArray(12) { 0 }
        filtered.forEach { p ->
            val cal = Calendar.getInstance()
            cal.timeInMillis = p.tanggal
            val month = cal.get(Calendar.MONTH) // 0-indexed
            monthlyCounts[month]++
        }
        val monthlyData = monthNames.mapIndexed { index, name ->
            MonthlyChartData(month = name, count = monthlyCounts[index])
        }

        // 2. Category data
        val categoryCount = mutableMapOf<String, Int>()
        filtered.forEach { p ->
            val cat = p.kategoriName.ifEmpty { "Tidak Diketahui" }
            categoryCount[cat] = (categoryCount[cat] ?: 0) + 1
        }
        val totalCat = filtered.size.coerceAtLeast(1)
        val categoryData = categoryCount.entries
            .sortedByDescending { it.value }
            .take(6)
            .map { (cat, cnt) ->
                CategoryChartData(
                    category = cat,
                    count = cnt,
                    percentage = cnt.toFloat() / totalCat
                )
            }

        // 3. Jurusan data
        val jurusanCount = mutableMapOf<String, Int>()
        filtered.forEach { p ->
            val major = majorMap[p.nimMahasiswa] ?: "Lainnya"
            jurusanCount[major] = (jurusanCount[major] ?: 0) + 1
        }
        val totalJurusan = filtered.size.coerceAtLeast(1)
        val jurusanData = jurusanCount.entries
            .sortedByDescending { it.value }
            .map { (jurusan, cnt) ->
                JurusanChartData(
                    jurusan = jurusan,
                    count = cnt,
                    percentage = cnt.toFloat() / totalJurusan
                )
            }

        _uiState.value = _uiState.value.copy(
            isLoading = false,
            selectedYear = selectedYear,
            availableYears = availableYears,
            totalPelanggaran = filtered.size,
            monthlyData = monthlyData,
            categoryData = categoryData,
            jurusanData = jurusanData
        )
    }
}
