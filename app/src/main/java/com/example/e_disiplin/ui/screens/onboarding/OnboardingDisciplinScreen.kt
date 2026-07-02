package com.example.e_disiplin.ui.screens.onboarding

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import com.example.e_disiplin.ui.theme.EDisiplinTheme
import kotlinx.coroutines.launch

// Colors
private val NavyDark = Color(0xFF1A2154)
private val NavyMid = Color(0xFF26327A)
private val GoldAccent = Color(0xFFF2B705)
private val ButtonBlue = Color(0xFF4C5FD5)
private val TextGray = Color(0xFF8A8D9E)
private val BgCream = Color(0xFFF7F5F0)
private val TextNavy = Color(0xFF161B33)

data class OnboardingPageData(
    val title: String,
    val description: String
)

@Composable
fun OnboardingDisciplinScreen(onFinishOnboarding: () -> Unit) {
    
    val pages = listOf(
        OnboardingPageData(
            title = "Selamat Datang",
            description = "Sistem E-Disiplin membantu memantau dan mencatat kedisiplinan mahasiswa dengan mudah."
        ),
        OnboardingPageData(
            title = "Pencatatan Real-time",
            description = "Laporkan pelanggaran atau prestasi secara langsung, didukung oleh bukti foto."
        ),
        OnboardingPageData(
            title = "Transparan dan Akurat",
            description = "Riwayat pelanggaran tersimpan aman dan mudah ditelusuri kapan saja."
        )
    )
    
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgCream)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal))
    ) {
        // HERO SECTION (top ~55%)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.55f)
                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
        ) {
            // Figma Background Asset
            Image(
                painter = painterResource(id = R.drawable.bg_onboarding_hero),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal))
            ) {
                // Top row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = GoldAccent, fontWeight = FontWeight.Bold)) {
                                append("E-")
                            }
                            withStyle(style = SpanStyle(color = Color.White, fontWeight = FontWeight.Bold)) {
                                append("DISIPLIN")
                            }
                        },
                        fontSize = 22.sp
                    )

                    // Lewati button
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.1f))
                            .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                            .clickable { onFinishOnboarding() }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Lewati",
                            color = Color(0xFFA9B8D6),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                
                // Centered content
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    // Inner filled translucent circle with border
                    Box(
                        modifier = Modifier
                            .size(255.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.05f))
                            .border(2.dp, Color(0xFF485885), CircleShape)
                    )
                    
                    // Inside white filled circle
                    Box(
                        modifier = Modifier
                            .size(215.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_onboarding_bear),
                            contentDescription = "Onboarding Bear",
                            modifier = Modifier
                                .size(200.dp)
                                .offset(y = (-4).dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
        }
        
        // BOTTOM SECTION (bottom ~45%)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.45f)
                .padding(horizontal = 24.dp)
                .padding(top = 32.dp, bottom = 24.dp)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                Column {
                    Text(
                        text = pages[page].title,
                        color = TextNavy,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text(
                        text = pages[page].description,
                        color = TextGray,
                        fontSize = 15.sp,
                        lineHeight = 22.sp
                    )
                }
            }
            
            // Page indicator row
            Row(
                modifier = Modifier.padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(pages.size) { iteration ->
                    val color = if (pagerState.currentPage == iteration) Color(0xFF1B2154) else Color(0xFFD9D9E3)
                    val width = if (pagerState.currentPage == iteration) 28.dp else 8.dp
                    
                    Box(
                        modifier = Modifier
                            .size(width = width, height = 8.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                }
            }
            
            // CTA Button
            Button(
                onClick = {
                    if (pagerState.currentPage == pages.size - 1) {
                        onFinishOnboarding()
                    } else {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(4.dp, RoundedCornerShape(28.dp)),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A3F7B))
            ) {
                Text(
                    text = if (pagerState.currentPage == pages.size - 1) "Mulai" else "Selanjutnya",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Back Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                contentAlignment = Alignment.Center
            ) {
                if (pagerState.currentPage > 0) {
                    Row(
                        modifier = Modifier
                            .clickable {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                }
                            }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color(0xFF6F7789),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Kembali",
                            color = Color(0xFF6F7789),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingDisciplinScreenPreview() {
    EDisiplinTheme {
        OnboardingDisciplinScreen(onFinishOnboarding = {})
    }
}
