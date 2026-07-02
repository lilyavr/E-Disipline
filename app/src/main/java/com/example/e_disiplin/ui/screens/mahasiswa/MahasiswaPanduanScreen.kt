package com.example.e_disiplin.ui.screens.mahasiswa

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val BgCream = Color(0xFFF9F9F4)
private val TextNavy = Color(0xFF1B2154)
private val TextGray = Color(0xFF8A8D9E)

@Composable
fun MahasiswaPanduanScreen(onNavigateBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgCream)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal))
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back",
                tint = TextNavy,
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .clickable { onNavigateBack() }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Panduan Pengguna",
                color = TextNavy,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            PanduanItem(
                title = "Beranda",
                description = "Lihat ringkasan total poin, jadwal kuliah hari ini, dan riwayat pelanggaran terbaru."
            )
            PanduanItem(
                title = "Scan QR Pelanggaran",
                description = "Scan QR Code yang ditunjukkan admin saat terjadi pelanggaran. Catatan bersifat permanen."
            )
            PanduanItem(
                title = "Riwayat",
                description = "Lihat semua pelanggaran Anda. Filter berdasarkan bulan untuk menelusuri riwayat."
            )
            PanduanItem(
                title = "Notifikasi",
                description = "Terima pemberitahuan real-time setiap kali admin mencatat poin pelanggaran baru."
            )
            PanduanItem(
                title = "Profil",
                description = "Lihat data diri Anda. Perubahan data hanya bisa dilakukan oleh admin."
            )
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun PanduanItem(title: String, description: String) {
    Column(modifier = Modifier.padding(bottom = 24.dp)) {
        Text(
            text = title,
            color = TextNavy,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            color = TextGray,
            fontSize = 14.sp,
            lineHeight = 22.sp
        )
    }
}
