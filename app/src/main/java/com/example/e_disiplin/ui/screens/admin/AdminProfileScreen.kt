package com.example.e_disiplin.ui.screens.admin

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
private val GlassBlue = Color(0xFF33457A)
private val RedAccent = Color(0xFFE95B5B)
private val RedLightBg = Color(0xFFFDECEB)

@Composable
fun AdminProfileScreen(
    onLogoutClick: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToMahasiswa: () -> Unit = {},
    onNavigateToPanduan: () -> Unit = {}
) {
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
            // TOP HERO SECTION
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            ) {
                // Background Asset
                Image(
                    painter = painterResource(id = R.drawable.bg_main_hero),
                    contentDescription = "Background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )

                // Hero Content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal))
                        .padding(vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .border(3.dp, GoldAccent, CircleShape)
                            .padding(4.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_onboarding_bear),
                            contentDescription = "Avatar",
                            modifier = Modifier.size(64.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Name
                    Text(
                        text = "Admin UVERS",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // NIK
                    Text(
                        text = "NIK: 1234567890123456",
                        color = Color(0xFFD9D9E3),
                        fontSize = 13.sp
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Role Pill
                    Box(
                        modifier = Modifier
                            .border(1.dp, GoldAccent, RoundedCornerShape(20.dp))
                            .background(GlassBlue, RoundedCornerShape(20.dp))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🧑‍💼", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Administrator",
                                color = GoldAccent,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            
            // BOTTOM SETTINGS SECTION
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                
                SettingsCard(
                    icon = { Icon(Icons.Filled.Person, contentDescription = null, tint = Color(0xFF485885), modifier = Modifier.size(24.dp)) },
                    title = "Kelola Akun Admin",
                    subtitle = "Ubah data & password",
                    onClick = { /* TODO */ }
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                SettingsCard(
                    icon = { Text("🎓", fontSize = 20.sp) },
                    title = "Manajemen Mahasiswa",
                    subtitle = "Daftar & data mahasiswa",
                    onClick = onNavigateToMahasiswa
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                SettingsCard(
                    icon = { Text("📊", fontSize = 20.sp) },
                    title = "Laporan & Statistik",
                    subtitle = "Rekap pelanggaran bulanan",
                    onClick = { /* TODO */ }
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                SettingsCard(
                    icon = { Icon(Icons.Filled.Settings, contentDescription = null, tint = Color(0xFF485885), modifier = Modifier.size(24.dp)) },
                    title = "Pengaturan Sistem",
                    subtitle = "Konfigurasi E-DISIPLIN",
                    onClick = onNavigateToSettings
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                SettingsCard(
                    icon = { Text("❓", fontSize = 20.sp) },
                    title = "Bantuan & Panduan",
                    subtitle = "Cara penggunaan aplikasi",
                    onClick = onNavigateToPanduan
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Logout Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .border(1.dp, RedAccent.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                        .background(RedLightBg, RoundedCornerShape(16.dp))
                        .clickable { onLogoutClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🚪", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Keluar dari Akun",
                            color = RedAccent,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Footer
                Text(
                    text = "E-DISIPLIN v1.0 • Universitas Universal",
                    color = Color(0xFFC4C4CD),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun SettingsCard(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Container
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF3F3F6)),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Text Content
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = TextNavy,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    color = TextGray,
                    fontSize = 12.sp
                )
            }
            
            // Arrow Right
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Lihat",
                tint = Color(0xFFC4C4CD),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAdminProfileScreen() {
    AdminProfileScreen()
}
