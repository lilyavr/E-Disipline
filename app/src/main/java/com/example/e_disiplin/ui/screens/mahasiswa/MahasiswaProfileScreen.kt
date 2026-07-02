package com.example.e_disiplin.ui.screens.mahasiswa

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.e_disiplin.R
import com.example.e_disiplin.ui.screens.admin.SettingsCard

// Colors
private val BgCream = Color(0xFFF9F9F4)
private val GoldAccent = Color(0xFFF2B705)
private val GlassBlue = Color(0xFF33457A)
private val RedAccent = Color(0xFFE95B5B)
private val RedLightBg = Color(0xFFFDECEB)
private val TextNavy = Color(0xFF1B2154)

@Composable
fun MahasiswaProfileScreen(onLogoutClick: () -> Unit = {}, onNavigatePanduan: () -> Unit = {}) {
    var showProfileDialog by remember { mutableStateOf(false) }

    if (showProfileDialog) {
        Dialog(
            onDismissRequest = { showProfileDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(modifier = Modifier.padding(24.dp)) {
                ProfileDetailCard()
            }
        }
    }
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
                        text = "Lily",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // NIM
                    Text(
                        text = "NIM: 2022133010",
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
                            Text("🎓", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Mahasiswa Aktif",
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
                    title = "Profil Saya",
                    subtitle = "Lihat data diri Anda",
                    onClick = { showProfileDialog = true }
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                SettingsCard(
                    icon = { 
                        Text(
                            text = "?", 
                            color = RedAccent, 
                            fontSize = 20.sp, 
                            fontWeight = FontWeight.ExtraBold
                        ) 
                    },
                    title = "Bantuan",
                    subtitle = "Panduan penggunaan aplikasi",
                    onClick = { onNavigatePanduan() }
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
fun ProfileDetailCard() {
    androidx.compose.material3.Card(
        shape = RoundedCornerShape(16.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color.White),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            // NAMA LENGKAP
            ProfileField(label = "NAMA LENGKAP", value = "Lily")
            HorizontalDivider(color = Color(0xFFF3F3F6), thickness = 1.dp, modifier = Modifier.padding(vertical = 12.dp))
            
            // NIM
            ProfileField(label = "NIM", value = "2022133010")
            HorizontalDivider(color = Color(0xFFF3F3F6), thickness = 1.dp, modifier = Modifier.padding(vertical = 12.dp))
            
            // PROGRAM STUDI
            ProfileField(label = "PROGRAM STUDI", value = "Teknik Perangkat Lunak")
            HorizontalDivider(color = Color(0xFFF3F3F6), thickness = 1.dp, modifier = Modifier.padding(vertical = 12.dp))
            
            // SEMESTER
            ProfileField(label = "SEMESTER", value = "Semester 8")
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Info Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF3F3F6), RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Perubahan data diri hanya dapat dilakukan oleh ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = TextNavy)) {
                            append("Admin E-DISIPLIN")
                        }
                        append(".")
                    },
                    color = Color(0xFF8A8D9E), // Gray text
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
fun ProfileField(label: String, value: String) {
    Column {
        Text(
            text = label, 
            color = Color(0xFF8A8D9E), 
            fontSize = 12.sp, 
            fontWeight = FontWeight.Bold, 
            letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value, 
            color = TextNavy, 
            fontSize = 16.sp, 
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMahasiswaProfileScreen() {
    MahasiswaProfileScreen()
}
