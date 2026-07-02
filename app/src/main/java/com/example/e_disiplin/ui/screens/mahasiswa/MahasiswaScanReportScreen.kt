package com.example.e_disiplin.ui.screens.mahasiswa

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.e_disiplin.domain.model.Pelanggaran

private val BgLightBlue = Color(0xFFF4F7FC)
private val TextNavy = Color(0xFF1B2154)
private val TextGray = Color(0xFF8A8D9E)
private val CardBorder = Color(0xFFE5E5EA)
private val ButtonActiveBg = Color(0xFF2A3F7B)
private val YellowBadgeBg = Color(0xFFFFEFA6)
private val YellowBadgeText = Color(0xFFB38600)

@Composable
fun MahasiswaScanReportScreen(
    pelanggaranId: String,
    jenis: String,
    mahasiswaName: String,
    nim: String,
    waktu: String,
    onNavigateBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgLightBlue)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            // Icon / Header
            Box(
                modifier = Modifier.size(64.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("📱", fontSize = 48.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Pindai Berhasil",
                color = TextNavy,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Main Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "QR PELANGGARAN\nTERPINDAI",
                        color = TextNavy,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    ReportRow(label = "ID Pelanggaran", value = pelanggaranId)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBorder)
                    ReportRow(label = "Jenis", value = jenis)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBorder)
                    ReportRow(label = "Mahasiswa", value = "$mahasiswaName\n($nim)")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBorder)
                    ReportRow(label = "Waktu", value = waktu)
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp),
                        color = TextNavy,
                        strokeWidth = 3.dp
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Menunggu Verifikasi Admin...",
                        color = TextGray,
                        fontSize = 14.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Status Badge
            Box(
                modifier = Modifier
                    .background(YellowBadgeBg, RoundedCornerShape(24.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "MENUNGGU VERIFIKASI",
                    color = YellowBadgeText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Data pelanggaran sedang diverifikasi oleh Admin. Mohon tunggu status berikutnya.",
                color = TextGray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Back Button
            Button(
                onClick = onNavigateBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = TextNavy
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, TextNavy)
            ) {
                Text("KEMBALI KE BERANDA", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ReportRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = TextNavy,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.4f)
        )
        Text(
            text = value,
            color = TextGray,
            fontSize = 14.sp,
            modifier = Modifier.weight(0.6f)
        )
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────

/**
 * Full-screen preview of MahasiswaScanReportScreen with sample scan data.
 * No ViewModel or Firebase needed — all params are plain values.
 */
@Preview(showBackground = true, showSystemUi = true, name = "Scan Report – Keterlambatan")
@Composable
fun PreviewMahasiswaScanReportKeterlambatan() {
    MahasiswaScanReportScreen(
        pelanggaranId = "AB12CD34",
        jenis = "Telat Masuk Kelas",
        mahasiswaName = "Andi Pratama",
        nim = "2021133001",
        waktu = "02 Jul 2026 | 08:15 WIB",
        onNavigateBack = {}
    )
}

@Preview(showBackground = true, showSystemUi = true, name = "Scan Report – Pelanggaran Umum")
@Composable
fun PreviewMahasiswaScanReportUmum() {
    MahasiswaScanReportScreen(
        pelanggaranId = "XY98ZW10",
        jenis = "Pelanggaran Umum",
        mahasiswaName = "Citra Dewi",
        nim = "2021133003",
        waktu = "02 Jul 2026 | 10:30 WIB",
        onNavigateBack = {}
    )
}
