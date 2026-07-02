package com.example.e_disiplin.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.e_disiplin.domain.model.KategoriPelanggaran
import com.example.e_disiplin.domain.model.Pelanggaran
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val BgCream = Color(0xFFF9F9F4)
private val TextNavy = Color(0xFF1B2154)
private val TextGray = Color(0xFF8A8D9E)
private val GoldAccent = Color(0xFFF2B705)

@Composable
fun AdminNotificationScreen(
    viewModel: AdminNotificationViewModel = viewModel()
) {
    val pendingList by viewModel.pendingPelanggaran.collectAsState()
    val mahasiswaMap by viewModel.mahasiswaMap.collectAsState()
    val kategoriList by viewModel.kategoriList.collectAsState()

    var selectedPelanggaran by remember { mutableStateOf<Pelanggaran?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgCream)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp)
            ) {
                Text(
                    text = "Notifikasi",
                    color = TextNavy,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (pendingList.isEmpty()) "Belum ada notifikasi baru" else "${pendingList.size} scan menunggu verifikasi",
                    color = TextGray,
                    fontSize = 14.sp
                )
            }

            if (pendingList.isEmpty()) {
                EmptyNotificationState()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(pendingList) { pelanggaran ->
                        val mahasiswa = mahasiswaMap[pelanggaran.nimMahasiswa]
                        val name = mahasiswa?.name ?: "Memuat..."
                        PendingScanCard(
                            pelanggaran = pelanggaran,
                            mahasiswaName = name,
                            onVerifyClick = {
                                selectedPelanggaran = pelanggaran
                                showDialog = true
                            }
                        )
                    }
                }
            }
        }
        
        if (showDialog && selectedPelanggaran != null) {
            val mahasiswaName = mahasiswaMap[selectedPelanggaran!!.nimMahasiswa]?.name ?: ""
            VerificationDialog(
                pelanggaran = selectedPelanggaran!!,
                mahasiswaName = mahasiswaName,
                kategoriList = kategoriList,
                onDismiss = {
                    showDialog = false
                    selectedPelanggaran = null
                },
                onSubmit = { katId, katName, tingkat, poin ->
                    viewModel.verifyPelanggaran(
                        pelanggaranId = selectedPelanggaran!!.id,
                        nimMahasiswa = selectedPelanggaran!!.nimMahasiswa,
                        kategoriId = katId,
                        kategoriName = katName,
                        tingkat = tingkat,
                        poin = poin,
                        onSuccess = {
                            showDialog = false
                            selectedPelanggaran = null
                        },
                        onError = {
                            // Optionally handle error
                            showDialog = false
                            selectedPelanggaran = null
                        }
                    )
                }
            )
        }
    }
}

@Composable
fun EmptyNotificationState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 48.dp, horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color(0xFFFFFDF5), CircleShape)
                        .border(1.dp, GoldAccent.copy(alpha = 0.3f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = "Bell",
                        tint = GoldAccent,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Tidak Ada Notifikasi",
                    color = TextNavy,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Belum ada aktivitas atau riwayat pelanggaran\nterbaru dari mahasiswa.",
                    color = TextGray,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
fun PendingScanCard(
    pelanggaran: Pelanggaran,
    mahasiswaName: String,
    onVerifyClick: () -> Unit
) {
    val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
    val waktu = formatter.format(Date(pelanggaran.tanggal))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Scan Masuk",
                        color = GoldAccent,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = pelanggaran.kategoriName, // "Telat Masuk Kelas" or "Pelanggaran Umum"
                        color = TextNavy,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = waktu,
                    color = TextGray,
                    fontSize = 12.sp
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFF3F3F6))
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = mahasiswaName,
                        color = TextNavy,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "NIM: ${pelanggaran.nimMahasiswa}",
                        color = TextGray,
                        fontSize = 12.sp
                    )
                }
                
                Button(
                    onClick = onVerifyClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TextNavy),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("Verifikasi", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun VerificationDialog(
    pelanggaran: Pelanggaran,
    mahasiswaName: String,
    kategoriList: List<KategoriPelanggaran>,
    onDismiss: () -> Unit,
    onSubmit: (String, String, String, Int) -> Unit
) {
    // Attempt to match the initial category if possible
    var selectedKategori by remember { 
        mutableStateOf(
            kategoriList.find { it.nama == pelanggaran.kategoriName } 
                ?: kategoriList.firstOrNull()
        )
    }
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .clickable { /* prevent dismiss when clicking inside */ },
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Verifikasi Pelanggaran",
                    color = TextNavy,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Mahasiswa: $mahasiswaName (${pelanggaran.nimMahasiswa})",
                    color = TextGray,
                    fontSize = 14.sp
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Pilih Kategori Pelanggaran:",
                    color = TextNavy,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                // Dropdown
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFE5E5EA), RoundedCornerShape(8.dp))
                        .background(Color(0xFFF9F9F4), RoundedCornerShape(8.dp))
                        .clickable { expanded = true }
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedKategori?.nama ?: "Pilih Kategori...",
                            color = if (selectedKategori != null) TextNavy else TextGray,
                            fontSize = 14.sp
                        )
                        Icon(Icons.Filled.ArrowDropDown, contentDescription = null, tint = TextGray)
                    }
                    
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth(0.8f).background(Color.White)
                    ) {
                        kategoriList.forEach { kategori ->
                            DropdownMenuItem(
                                text = { Text(kategori.nama, color = TextNavy) },
                                onClick = {
                                    selectedKategori = kategori
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Point summary
                if (selectedKategori != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Tingkat: ${selectedKategori!!.tingkat.replaceFirstChar { it.uppercase() }}",
                                color = TextGray,
                                fontSize = 14.sp
                            )
                        }
                        Text(
                            text = "+${selectedKategori!!.poin} Poin",
                            color = Color(0xFFE95B5B),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Batal", color = TextGray)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            selectedKategori?.let { kat ->
                                onSubmit(kat.id, kat.nama, kat.tingkat, kat.poin)
                            }
                        },
                        enabled = selectedKategori != null,
                        colors = ButtonDefaults.buttonColors(containerColor = TextNavy)
                    ) {
                        Text("Terima & Verifikasi")
                    }
                }
            }
        }
    }
}
