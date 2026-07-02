package com.example.e_disiplin.ui.screens.mahasiswa

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.e_disiplin.domain.model.Pelanggaran
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val BgCream = Color(0xFFF9F9F4)
private val TextNavy = Color(0xFF1B2154)
private val TextGray = Color(0xFF8A8D9E)
private val GoldAccent = Color(0xFFF2B705)
private val GreenAccent = Color(0xFF4CAF50)
private val RedAccent = Color(0xFFE95B5B)

@Composable
fun MahasiswaNotificationScreen(
    viewModel: MahasiswaDashboardViewModel
) {
    val verifiedList by viewModel.verifiedPelanggaran.collectAsState()

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
            text = "Notifikasi",
            color = TextNavy,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = if (verifiedList.isEmpty()) "Belum ada notifikasi" else "${verifiedList.size} pelanggaran telah diverifikasi",
            color = TextGray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (verifiedList.isEmpty()) {
            // Empty State
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
                        text = "Anda belum memiliki catatan pelanggaran.\nPertahankan kedisiplinan Anda!",
                        color = TextGray,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(verifiedList) { pelanggaran ->
                    VerifiedNotificationCard(pelanggaran)
                }
            }
        }
    }
}

@Composable
fun VerifiedNotificationCard(pelanggaran: Pelanggaran) {
    val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.forLanguageTag("id-ID"))
    val waktu = formatter.format(Date(pelanggaran.tanggal))

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
                // Green check icon
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

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Pelanggaran Diverifikasi ✓",
                        color = GreenAccent,
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

// ── Previews ──────────────────────────────────────────────────────────────────

private val sampleNotifPelanggaran = listOf(
    Pelanggaran(
        id = "abc123",
        nimMahasiswa = "2021133001",
        kategoriId = "k1",
        kategoriName = "Telat Masuk Kelas",
        tingkat = "ringan",
        poin = 5,
        status = "Verified",
        tanggal = System.currentTimeMillis() - 3_600_000L
    ),
    Pelanggaran(
        id = "def456",
        nimMahasiswa = "2021133001",
        kategoriId = "k2",
        kategoriName = "Tidak Memakai Seragam",
        tingkat = "sedang",
        poin = 10,
        status = "Verified",
        tanggal = System.currentTimeMillis() - 86_400_000L
    ),
    Pelanggaran(
        id = "ghi789",
        nimMahasiswa = "2021133001",
        kategoriId = "k3",
        kategoriName = "Pelanggaran Umum",
        tingkat = "berat",
        poin = 25,
        status = "Verified",
        tanggal = System.currentTimeMillis() - 172_800_000L
    )
)

/**
 * Preview renders the notification list with static sample data.
 * No ViewModel or Firebase is needed.
 */
@Preview(showBackground = true, showSystemUi = true, name = "Notifikasi Mahasiswa – Ada Data")
@Composable
fun PreviewMahasiswaNotificationWithData() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgCream)
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Notifikasi",
            color = TextNavy,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Riwayat pelanggaran terverifikasi",
            color = TextGray,
            fontSize = 13.sp
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(sampleNotifPelanggaran, key = { it.id }) { p ->
                VerifiedNotificationCard(pelanggaran = p)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Notifikasi Mahasiswa – Kosong")
@Composable
fun PreviewMahasiswaNotificationEmpty() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgCream)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Notifications,
            contentDescription = null,
            tint = TextGray.copy(alpha = 0.3f),
            modifier = Modifier.size(72.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Belum ada notifikasi",
            color = TextGray,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}
