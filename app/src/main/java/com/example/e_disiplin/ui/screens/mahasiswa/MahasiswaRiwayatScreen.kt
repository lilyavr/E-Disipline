package com.example.e_disiplin.ui.screens.mahasiswa

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.e_disiplin.domain.model.Pelanggaran
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val BgCream = Color(0xFFF9F9F4)
private val TextNavy = Color(0xFF1B2154)
private val TextGray = Color(0xFF8A8D9E)
private val InputBorder = Color(0xFFE5E5EA)
private val GreenAccent = Color(0xFF4CAF50)
private val RedAccent = Color(0xFFE95B5B)
private val OrangeAccent = Color(0xFFF2B705)

@Composable
fun MahasiswaRiwayatScreen(viewModel: MahasiswaDashboardViewModel = viewModel()) {
    val allPelanggaran by viewModel.allPelanggaran.collectAsState()
    
    var selectedMonth by remember { mutableStateOf<Int?>(null) } // null means "Semua", 0-11 for Jan-Dec
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val months = listOf(
        "Januari", "Februari", "Maret", "April", "Mei", "Juni", 
        "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    )

    val filteredPelanggaran = if (selectedMonth == null) {
        allPelanggaran
    } else {
        allPelanggaran.filter { pelanggaran ->
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = pelanggaran.tanggal
            calendar.get(Calendar.MONTH) == selectedMonth
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgCream)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal))
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Header
        Text(
            text = "Riwayat Pelanggaran",
            color = TextNavy,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Catatan pelanggaran tata tertib Anda",
            color = TextGray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Filters Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // "Semua" chip
            Box(
                modifier = Modifier
                    .background(
                        color = if (selectedMonth == null) TextNavy else Color.Transparent, 
                        shape = RoundedCornerShape(20.dp)
                    )
                    .border(
                        width = if (selectedMonth == null) 0.dp else 1.dp,
                        color = if (selectedMonth == null) Color.Transparent else InputBorder,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable { selectedMonth = null }
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Semua",
                    color = if (selectedMonth == null) Color.White else TextNavy,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // "Pilih Bulan" dropdown
            Box(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, if (selectedMonth != null) TextNavy else InputBorder, RoundedCornerShape(20.dp))
                    .background(Color.White, RoundedCornerShape(20.dp))
                    .clickable { isDropdownExpanded = true }
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (selectedMonth == null) "Pilih Bulan" else months[selectedMonth!!],
                        color = TextNavy,
                        fontSize = 14.sp,
                        fontWeight = if (selectedMonth != null) FontWeight.Bold else FontWeight.Normal
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Dropdown",
                        tint = TextNavy,
                        modifier = Modifier.size(20.dp)
                    )
                }

                DropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    months.forEachIndexed { index, month ->
                        DropdownMenuItem(
                            text = { Text(month, color = TextNavy) },
                            onClick = {
                                selectedMonth = index
                                isDropdownExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (filteredPelanggaran.isEmpty()) {
            // Empty State Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "📭",
                        fontSize = 48.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Tidak ada pelanggaran",
                        color = TextNavy,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tidak ada catatan pada Semua",
                        color = TextGray,
                        fontSize = 12.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredPelanggaran) { pelanggaran ->
                    RiwayatPelanggaranCard(pelanggaran)
                }
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun RiwayatPelanggaranCard(pelanggaran: Pelanggaran) {
    val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.forLanguageTag("id-ID"))
    val waktu = formatter.format(Date(pelanggaran.tanggal))
    
    val isVerified = pelanggaran.status == "Verified"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Status Icon
                if (isVerified) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color(0xFFE6F4EA), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Verified",
                            tint = GreenAccent,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color(0xFFFFF9E6), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "Pending",
                            tint = OrangeAccent,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isVerified) "Diverifikasi Admin ✓" else "Menunggu Verifikasi ⏳",
                        color = if (isVerified) GreenAccent else OrangeAccent,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = pelanggaran.kategoriName.ifEmpty { "Pelanggaran Umum" },
                        color = TextNavy,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (isVerified && pelanggaran.poin > 0) {
                    // Points badge
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFEEEE), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "+${pelanggaran.poin} pts",
                            color = RedAccent,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFF3F3F6))
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = waktu,
                    color = TextGray,
                    fontSize = 12.sp
                )

                if (isVerified) {
                    // Tingkat badge
                    val tingkat = pelanggaran.tingkat
                    val tingkatColor = when (tingkat.lowercase()) {
                        "ringan" -> Color(0xFF4CAF50)
                        "sedang" -> Color(0xFFF2B705)
                        "berat" -> Color(0xFFE95B5B)
                        else -> TextGray
                    }
                    Box(
                        modifier = Modifier
                            .background(tingkatColor.copy(alpha = 0.12f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = tingkat.replaceFirstChar { it.uppercase() }.ifEmpty { "Pelanggaran" },
                            color = tingkatColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMahasiswaRiwayatScreen() {
    MahasiswaRiwayatScreen()
}
