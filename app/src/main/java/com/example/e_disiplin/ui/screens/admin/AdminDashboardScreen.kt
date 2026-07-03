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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.e_disiplin.R

// Colors
private val BgCream = Color(0xFFF9F9F4)
private val TextNavy = Color(0xFF1B2154)
private val TextGray = Color(0xFF8A8D9E)
private val GoldAccent = Color(0xFFF2B705)
private val GreenAccent = Color(0xFF4CAF50)
private val RedAccent = Color(0xFFE95B5B)
private val GlassBlue = Color(0xFF33457A)

@Composable
fun AdminDashboardScreen(
    viewModel: AdminMahasiswaViewModel = viewModel()
) {
    var currentTime by remember { mutableStateOf(LocalDateTime.now()) }
    var activeQrDrawable by remember { mutableStateOf<Int?>(null) }
    
    val totalMahasiswa by viewModel.totalMahasiswa.collectAsState()
    val pendingCount by viewModel.pendingCount.collectAsState()
    val todayCount by viewModel.todayCount.collectAsState()
    val verifiedMonthCount by viewModel.verifiedMonthCount.collectAsState()

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            currentTime = LocalDateTime.now()
        }
    }

    val timeFormatter = DateTimeFormatter.ofPattern("HH.mm.ss")
    val timeString = currentTime.format(timeFormatter)

    val dayFormatter = DateTimeFormatter.ofPattern("EEEE", Locale.forLanguageTag("id-ID"))
    val dayString = currentTime.format(dayFormatter).uppercase()

    val dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.forLanguageTag("id-ID"))
    val dateString = currentTime.format(dateFormatter)

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
                                text = "Selamat datang, Admin 👋",
                                color = Color(0xFFD9D9E3),
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Admin UVERS",
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Sistem E-DISIPLIN • UVERS",
                                color = Color(0xFFA0A3B1),
                                fontSize = 11.sp
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
                                    text = timeString,
                                    color = GoldAccent,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = dayString,
                                    color = Color(0xFFD9D9E3),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = dateString,
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // 3 Summary Cards
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SummaryCard(
                            modifier = Modifier.weight(1f),
                            number = todayCount.toString(),
                            numberColor = GoldAccent,
                            label = "Hari Ini"
                        )
                        SummaryCard(
                            modifier = Modifier.weight(1f),
                            number = verifiedMonthCount.toString(),
                            numberColor = GreenAccent,
                            label = "Bulan Ini"
                        )
                        SummaryCard(
                            modifier = Modifier.weight(1f),
                            number = pendingCount.toString(),
                            numberColor = RedAccent,
                            label = "Pending"
                        )
                    }
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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ringkasan Data",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextNavy
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    GridCard(
                        modifier = Modifier.fillMaxWidth(),
                        icon = null,
                        iconBgColor = Color(0xFFF3F3F6),
                        number = totalMahasiswa.toString(),
                        numberColor = TextNavy,
                        label = "Total Mahasiswa"
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Text(
                    text = "Akses QR Scanner",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextNavy
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    QrActionCard(
                        modifier = Modifier.weight(1f),
                        title = "QR Keterlambatan",
                        icon = Icons.Filled.Schedule,
                        iconBgColor = Color(0xFFE8F0FE),
                        iconTint = Color(0xFF1B2154),
                        onClick = { activeQrDrawable = R.drawable.qr_keterlambatan }
                    )
                    QrActionCard(
                        modifier = Modifier.weight(1f),
                        title = "QR Pelanggaran",
                        icon = Icons.Filled.Warning,
                        iconBgColor = Color(0xFFFFF7E6),
                        iconTint = Color(0xFFF2B705),
                        onClick = { activeQrDrawable = R.drawable.qr_pelanggaran_umum }
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Full Screen QR Viewer
        if (activeQrDrawable != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .windowInsetsPadding(WindowInsets.safeDrawing),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Top bar with back button
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { activeQrDrawable = null }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Kembali", tint = TextNavy)
                        }
                        Text(
                            text = "Tutup QR",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextNavy
                        )
                    }
                    
                    // QR Image
                    Box(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = activeQrDrawable!!),
                            contentDescription = "QR Code",
                            modifier = Modifier.fillMaxWidth().padding(32.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QrActionCard(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    iconBgColor: Color = Color(0xFFF3F3F6),
    iconTint: Color = TextNavy,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(28.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(14.dp))
            
            Text(
                text = title,
                color = TextNavy,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun SummaryCard(modifier: Modifier = Modifier, number: String, numberColor: Color, label: String) {
    Box(
        modifier = modifier
            .background(GlassBlue, RoundedCornerShape(12.dp))
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = number,
                color = numberColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                color = Color(0xFFA0A3B1),
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun GridCard(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    iconTint: Color = Color.Black,
    iconBgColor: Color,
    number: String,
    numberColor: Color,
    label: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            if (icon != null) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(iconBgColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = iconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            Text(
                text = number,
                color = numberColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = label,
                color = TextGray,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAdminDashboardScreen() {
    AdminDashboardScreen()
}
