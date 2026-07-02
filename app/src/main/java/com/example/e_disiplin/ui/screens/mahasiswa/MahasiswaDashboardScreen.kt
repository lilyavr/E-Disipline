package com.example.e_disiplin.ui.screens.mahasiswa

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.e_disiplin.R
import com.example.e_disiplin.ui.screens.admin.GridCard

// Colors
private val BgCream = Color(0xFFF9F9F4)
private val TextNavy = Color(0xFF1B2154)
private val TextGray = Color(0xFF8A8D9E)
private val GoldAccent = Color(0xFFF2B705)
private val GreenAccent = Color(0xFF4CAF50)
private val RedAccent = Color(0xFFE95B5B)
private val GlassBlue = Color(0xFF33457A)

@Composable
fun MahasiswaDashboardScreen(
    nim: String = "",
    viewModel: MahasiswaDashboardViewModel = viewModel()
) {
    val mahasiswa by viewModel.mahasiswa.collectAsState()
    val totalPoin by viewModel.totalPoin.collectAsState()
    
    LaunchedEffect(nim) {
        viewModel.fetchMahasiswa(nim)
    }

    var currentTime by remember { mutableStateOf(LocalDateTime.now()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = LocalDateTime.now()
            delay(1000)
        }
    }

    val timeFormatter = DateTimeFormatter.ofPattern("HH.mm.ss", Locale("id", "ID"))
    val dayFormatter = DateTimeFormatter.ofPattern("EEEE", Locale("id", "ID"))
    val dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("id", "ID"))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgCream)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // HERO SECTION
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bg_main_hero),
                    contentDescription = "Background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal))
                        .padding(24.dp)
                ) {
                    // Header Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Selamat datang, Mahasiswa 👋",
                                color = Color(0xFFD9D9E3),
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = mahasiswa?.name ?: "Loading...",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "NIM: ${mahasiswa?.nim ?: "-"} • Sem. ${mahasiswa?.semester ?: "-"}",
                                color = Color(0xFFA0A3B1),
                                fontSize = 12.sp
                            )
                        }
                        
                        // Avatar
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .border(2.dp, GoldAccent, CircleShape)
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_onboarding_bear),
                                contentDescription = "Avatar",
                                modifier = Modifier.size(40.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Time Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                            .background(GlassBlue, RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "WAKTU SEKARANG",
                                    color = Color(0xFFD9D9E3),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = currentTime.format(timeFormatter),
                                    color = GoldAccent,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = currentTime.format(dayFormatter).uppercase(),
                                    color = Color(0xFFD9D9E3),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = currentTime.format(dateFormatter),
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Total Poin Pelanggaran
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                            .background(GlassBlue, RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Total Poin Pelanggaran",
                                    color = Color(0xFFD9D9E3),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text(
                                        text = totalPoin.toString(),
                                        color = if (totalPoin == 0) GreenAccent else RedAccent,
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "poin",
                                        color = Color(0xFFA0A3B1),
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(bottom = 6.dp)
                                    )
                                }
                                Text(
                                    text = if (totalPoin == 0) "Tidak ada catatan pelanggaran" else "Harap jaga kedisiplinan Anda",
                                    color = if (totalPoin == 0) GreenAccent else RedAccent,
                                    fontSize = 11.sp
                                )
                            }
                            
                            // Standing Badge
                            Box(
                                modifier = Modifier
                                    .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (totalPoin == 0) "Good Standing" else "$totalPoin Poin",
                                    color = if (totalPoin == 0) GreenAccent else RedAccent,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            
            // BOTTOM GRID SECTION
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    GridCard(
                        modifier = Modifier.weight(1f),
                        icon = "📊",
                        iconBgColor = Color(0xFFF3F3F6),
                        number = "0",
                        numberColor = GreenAccent, // Assuming green since it's 0 poin
                        label = "Total Poin"
                    )
                    GridCard(
                        modifier = Modifier.weight(1f),
                        icon = "✅",
                        iconBgColor = Color(0xFFE6F4EA),
                        number = "0",
                        numberColor = GreenAccent,
                        label = "Jml. Pelanggaran"
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    GridCard(
                        modifier = Modifier.weight(1f),
                        icon = "🛡️",
                        iconBgColor = Color(0xFFF9F5EC),
                        number = "Baik",
                        numberColor = GreenAccent,
                        label = "Status Disiplin"
                    )
                    GridCard(
                        modifier = Modifier.weight(1f),
                        icon = "📅",
                        iconBgColor = Color(0xFFF3F3F6),
                        number = mahasiswa?.semester ?: "-",
                        numberColor = TextNavy,
                        label = "Semester Aktif"
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // JADWAL KULIAH SECTION
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Jadwal Kuliah",
                            color = TextNavy,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Hari ini • ${currentTime.format(dateFormatter)}",
                            color = TextGray,
                            fontSize = 13.sp
                        )
                    }
                    
                    // Hari Ini Button
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFE5E5EA).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Hari Ini",
                            color = TextNavy,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Days Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val days = listOf(
                        DayItem("Min", isSelected = false, hasSchedule = false),
                        DayItem("Sen", isSelected = false, hasSchedule = true),
                        DayItem("Sel", isSelected = false, hasSchedule = true),
                        DayItem("Rab", isSelected = false, hasSchedule = false),
                        DayItem("Kam", isSelected = true, hasSchedule = true),
                        DayItem("Jum", isSelected = false, hasSchedule = false),
                        DayItem("Sab", isSelected = false, hasSchedule = false)
                    )
                    
                    days.forEach { day ->
                        DayPill(day, modifier = Modifier.weight(1f))
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Active Schedule Card Example
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Time column
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(50.dp)
                        ) {
                            Text(
                                text = "18.30",
                                color = TextNavy,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .height(1.dp)
                                    .background(Color(0xFFE5E5EA))
                            )
                            Text(
                                text = "21.50",
                                color = TextGray,
                                fontSize = 12.sp
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        // Details column
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Manajemen Sumber Daya Manusia",
                                color = TextNavy,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 20.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("📍", fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "R316 • 4 SKS",
                                    color = TextGray,
                                    fontSize = 13.sp
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        // "Malam ini" chip
                        Box(
                            modifier = Modifier
                                .border(1.dp, GoldAccent.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                .background(Color(0xFFFFFDF5), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Malam ini",
                                color = GoldAccent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Empty state card Example
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "✨", fontSize = 28.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Tidak Ada Perkuliahan",
                            color = TextNavy,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Kelas kosong — waktu belajar mandiri 📚",
                            color = TextGray,
                            fontSize = 13.sp
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(100.dp)) // Extra padding for bottom nav
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMahasiswaDashboardScreen() {
    MahasiswaDashboardScreen(nim = "2021133005")
}

data class DayItem(val name: String, val isSelected: Boolean, val hasSchedule: Boolean)

@Composable
fun DayPill(day: DayItem, modifier: Modifier = Modifier) {
    val bgColor = if (day.isSelected) TextNavy else Color.White
    val textColor = if (day.isSelected) GoldAccent else TextGray
    
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(
                width = 1.dp,
                color = if (day.isSelected) Color.Transparent else Color(0xFFE5E5EA),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = day.name,
                color = textColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .size(5.dp)
                    .clip(CircleShape)
                    .background(
                        if (day.hasSchedule) {
                            if (day.isSelected) GoldAccent else Color(0xFF485885)
                        } else Color.Transparent
                    )
            )
        }
    }
}
