package com.example.e_disiplin.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.e_disiplin.R

// Colors
private val BgCream = Color(0xFFF9F9F4)
private val TextNavy = Color(0xFF1B2154)
private val TextGray = Color(0xFF8A8D9E)
private val GoldAccent = Color(0xFFF2B705)
private val TagBg = Color(0xFFF3F3F6)
private val CardBorder = Color(0xFFE5E5EA)
private val ButtonDisabledBg = Color(0xFFD3D1D8)
private val ButtonDisabledText = Color(0xFF9E9CA7)
private val ButtonActiveBg = Color(0xFF2A3F7B)

@Composable
fun SelectRoleScreen(onRoleSelected: (String) -> Unit) {
    var selectedRole by remember { mutableStateOf<String?>(null) }

    Scaffold(
        bottomBar = {
            // Sticky CTA Button
            Box(
                modifier = Modifier
                    .background(BgCream)
                    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal))
                    .padding(24.dp)
            ) {
                Button(
                    onClick = { selectedRole?.let { onRoleSelected(it) } },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = selectedRole != null,
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonActiveBg,
                        contentColor = Color.White,
                        disabledContainerColor = ButtonDisabledBg,
                        disabledContentColor = ButtonDisabledText
                    )
                ) {
                    Text(
                        text = "Pilih Role untuk Melanjutkan",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { innerPadding ->
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BgCream)
        ) {
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = innerPadding.calculateBottomPadding())
            ) {
                // TOP HERO SECTION
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeight * 0.35f) // Take up 35% of the screen dynamically
                        .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                ) {
                    // Background Asset
                    Image(
                        painter = painterResource(id = R.drawable.bg_select_role),
                        contentDescription = "Background Select Role",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Hero Content
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal))
                            .padding(horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Logo Circle
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .border(2.dp, Color(0xFF5D6B9B), CircleShape) // thin border around logo
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_onboarding_bear),
                                contentDescription = "Logo",
                                modifier = Modifier.size(80.dp),
                                contentScale = ContentScale.Fit
                            )
                            

                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // E-DISIPLIN Text
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(color = GoldAccent)) {
                                    append("E-")
                                }
                                withStyle(style = SpanStyle(color = Color.White)) {
                                    append("DISIPLIN")
                                }
                            },
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Subtitle
                        Text(
                            text = "Sistem Pemantauan Kedisiplinan Mahasiswa",
                            color = Color(0xFFD9D9E3),
                            fontSize = 13.sp
                        )

                    }
                }

                // BOTTOM CONTENT SECTION
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = screenHeight * 0.65f)
                        .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
                        .padding(horizontal = 24.dp, vertical = 24.dp)
                ) {
                    Text(
                        text = "Pilih Role",
                        color = TextNavy,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(6.dp))
                    
                    Text(
                        text = "Masuk sesuai dengan akses yang diberikan",
                        color = TextGray,
                        fontSize = 14.sp
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))

                    // Admin Card
                    RoleCard(
                        title = "Admin",
                        description = "Kelola data pelanggaran mahasiswa, verifikasi laporan, dan pantau status disiplin.",
                        tags = listOf("Kelola Data", "Verifikasi", "Pantau"),
                        isSelected = selectedRole == "Admin",
                        onClick = { selectedRole = "Admin" }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))

                    // Mahasiswa Card
                    RoleCard(
                        title = "Mahasiswa",
                        description = "Lihat poin pelanggaran, notifikasi, dan ajukan klarifikasi.",
                        tags = listOf("Poin Saya", "Riwayat", "Notifikasi"),
                        isSelected = selectedRole == "Mahasiswa",
                        onClick = { selectedRole = "Mahasiswa" }
                    )
                }
            }
        }
    }
}

@Composable
fun RoleCard(
    title: String,
    description: String,
    tags: List<String>,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 0.dp),
        border = if (isSelected) BorderStroke(1.dp, TextNavy) else BorderStroke(1.dp, CardBorder)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Dummy Icon Area (Using a Box for now, replace with actual icon if provided)
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(BgCream),
                contentAlignment = Alignment.Center
            ) {
                Text(if (title == "Admin") "🧑‍💼" else "🎓", fontSize = 28.sp)
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = TextNavy,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = description,
                    color = TextGray,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Tags
                @OptIn(ExperimentalLayoutApi::class)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tags.forEach { tag ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(TagBg)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = tag,
                                color = TextGray,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = TextNavy,
                    unselectedColor = Color(0xFFD1D1D6)
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSelectRoleScreen() {
    SelectRoleScreen(onRoleSelected = {})
}
