package com.example.e_disiplin.ui.screens.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.e_disiplin.R
import com.example.e_disiplin.domain.model.Mahasiswa
import com.example.e_disiplin.domain.model.Pelanggaran
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val DTLNavy   = Color(0xFF1B2154)
private val DTLAccent = Color(0xFF2A3F7B)
private val DTLGold   = Color(0xFFF2B705)
private val DTLGray   = Color(0xFF8A8D9E)
private val DTLGreen  = Color(0xFF4CAF50)
private val DTLRed    = Color(0xFFE95B5B)
private val DTLBg     = Color(0xFFF0F3FB)

@Composable
fun AdminMahasiswaDetailScreen(
    viewModel: AdminMahasiswaViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    val mahasiswa by viewModel.selectedMahasiswa.collectAsState()
    val pelanggaranList by viewModel.selectedMahasiswaPelanggaran.collectAsState()

    if (mahasiswa == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Data tidak ditemukan", color = DTLGray)
        }
        return
    }

    val m = mahasiswa!!
    val verified   = pelanggaranList.filter { it.status == "Verified" }
    val pending    = pelanggaranList.filter { it.status == "Pending" }
    // Compute totalPoin live from Verified pelanggaran (real-time from Firebase)
    val totalPoin  = verified.sumOf { it.poin }
    val poinColor  = when {
        totalPoin < 25  -> DTLGreen
        totalPoin < 40  -> DTLGold
        else            -> DTLRed
    }
    
    val statusText = when {
        totalPoin < 10 -> "Aman"
        totalPoin < 25 -> "Waspada"
        totalPoin < 40 -> "Bahaya"
        else -> "Bahaya"
    }
    
    var showWarningDialog by remember(totalPoin) { mutableStateOf(totalPoin > 50) }

    Box(modifier = Modifier.fillMaxSize().background(DTLBg)) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {

            // ── Hero header ───────────────────────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.bg_main_hero),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.matchParentSize()
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .windowInsetsPadding(
                                WindowInsets.safeDrawing.only(
                                    WindowInsetsSides.Top + WindowInsetsSides.Horizontal
                                )
                            )
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Back button row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = onNavigateBack,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.15f))
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Kembali",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "Detail Mahasiswa",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Avatar
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .border(3.dp, DTLGold, CircleShape)
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color(0xFF2A3F7B), Color(0xFF3D5A99))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.Person,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(48.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(m.name, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("NIM: ${m.nim}", color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)

                        Spacer(modifier = Modifier.height(16.dp))

                        // Poin badge
                        Box(
                            modifier = Modifier
                                .border(1.dp, poinColor.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                                .background(poinColor.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                                .padding(horizontal = 18.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "$totalPoin Poin • $statusText",
                                color = poinColor,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Stat pills
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            StatPill(label = "Total", value = pelanggaranList.size.toString(), color = Color.White)
                            StatPill(label = "Verified", value = verified.size.toString(), color = DTLGreen)
                            StatPill(label = "Pending", value = pending.size.toString(), color = DTLRed)
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            // ── Bio card ──────────────────────────────────────────────
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
                        .padding(horizontal = 20.dp)
                ) {
                    SectionTitle("Informasi Mahasiswa")
                    Spacer(modifier = Modifier.height(12.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            InfoRow(Icons.Filled.Person,   "Program Studi", m.major)
                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFEEEEF2))
                            InfoRow(Icons.Filled.Schedule, "Semester",     "Semester ${m.semester}")
                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFEEEEF2))
                            InfoRow(Icons.Filled.Email,    "Email",        m.email.ifBlank { "-" })
                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFEEEEF2))
                            InfoRow(Icons.Filled.Phone,    "No. HP",       m.noHp.ifBlank { "-" })
                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFEEEEF2))
                            InfoRow(Icons.Filled.Schedule, "Tgl Lahir",   m.tanggalLahir.ifBlank { "-" })
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    SectionTitle("Riwayat Pelanggaran (${pelanggaranList.size})")
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // ── Pelanggaran list ──────────────────────────────────────
            if (pelanggaranList.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("✅", fontSize = 32.sp)
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    "Tidak ada riwayat pelanggaran",
                                    color = DTLGray,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            } else {
                items(pelanggaranList.sortedByDescending { it.tanggal }, key = { it.id }) { p ->
                    PelanggaranDetailCard(
                        pelanggaran = p,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
        
        if (showWarningDialog) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showWarningDialog = false },
                confirmButton = {
                    androidx.compose.material3.TextButton(onClick = { showWarningDialog = false }) {
                        Text("Tutup", color = DTLRed)
                    }
                },
                title = { Text("Peringatan Kritis!") },
                text = { Text("Mahasiswa ini telah melebihi 50 poin pelanggaran. Tindakan indisipliner lebih lanjut diperlukan.") },
                containerColor = Color.White,
                titleContentColor = DTLRed,
                textContentColor = DTLNavy
            )
        }
    }
}

// ── Sub-composables ───────────────────────────────────────────────────────────

@Composable
private fun SectionTitle(text: String) {
    Text(text = text, color = DTLNavy, fontSize = 16.sp, fontWeight = FontWeight.Bold)
}

@Composable
private fun StatPill(label: String, value: String, color: Color) {
    Box(
        modifier = Modifier
            .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, color = color, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
            Text(label, color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
        }
    }
}

@Composable
private fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFF0F3FB)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = DTLAccent, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(label, color = DTLGray, fontSize = 11.sp)
            Text(value, color = DTLNavy, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun PelanggaranDetailCard(pelanggaran: Pelanggaran, modifier: Modifier = Modifier) {
    val formatter = SimpleDateFormat("dd MMM yyyy • HH:mm", Locale.forLanguageTag("id-ID"))
    val dateStr = formatter.format(Date(pelanggaran.tanggal))

    val isVerified = pelanggaran.status == "Verified"
    val statusColor = if (isVerified) DTLGreen else DTLRed
    val statusBg    = if (isVerified) Color(0xFFE8F5E9) else Color(0xFFFDECEB)
    val statusLabel = if (isVerified) "Verified" else "Pending"

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Status dot
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(statusBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isVerified) Icons.Filled.CheckCircle else Icons.Filled.Schedule,
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    pelanggaran.kategoriName.ifBlank { "Pelanggaran Tidak Diketahui" },
                    color = DTLNavy,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(dateStr, color = DTLGray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Status badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(statusBg)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(statusLabel, color = statusColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    // Poin badge
                    if (isVerified && pelanggaran.poin > 0) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFFFF3E0))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text("+${pelanggaran.poin} poin", color = Color(0xFFE65100), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    if (pelanggaran.tingkat.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFE8EAF6))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(pelanggaran.tingkat, color = Color(0xFF3949AB), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
