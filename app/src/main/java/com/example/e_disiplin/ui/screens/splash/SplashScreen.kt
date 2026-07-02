package com.example.e_disiplin.ui.screens.splash

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.e_disiplin.R
import kotlinx.coroutines.delay

private val GoldAccent = Color(0xFFF2B705)
private val TextGray = Color(0xFF8A8D9E)

@Composable
fun SplashScreen(
    onNavigateNext: () -> Unit
) {
    var activeDotIndex by remember { mutableStateOf(0) }

    // Automatically navigate after 3 seconds, and animate dots
    LaunchedEffect(Unit) {
        val startTime = System.currentTimeMillis()
        while (System.currentTimeMillis() - startTime < 3000) {
            delay(300)
            activeDotIndex = (activeDotIndex + 1) % 4
        }
        onNavigateNext()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Full screen background
        Image(
            painter = painterResource(id = R.drawable.bg_splash_hero),
            contentDescription = "Splash Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        // Center Content
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Rings and Logo
            Box(contentAlignment = Alignment.Center) {
                // Outermost Ring
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .border(1.dp, GoldAccent.copy(alpha = 0.3f), CircleShape)
                )
                
                // Inner Ring
                Box(
                    modifier = Modifier
                        .size(210.dp)
                        .border(1.dp, GoldAccent.copy(alpha = 0.5f), CircleShape)
                )
                
                // White Circle with Bear
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_onboarding_bear),
                        contentDescription = "Logo",
                        modifier = Modifier.size(140.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

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
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Subtitle Line
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.width(32.dp).height(1.dp).background(TextGray.copy(alpha = 0.5f)))
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "UNIVERSITAS UNIVERSAL",
                    color = Color(0xFFC4C4CD),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.5.sp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Box(modifier = Modifier.width(32.dp).height(1.dp).background(TextGray.copy(alpha = 0.5f)))
            }
        }

        // Bottom Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Pager Dots with Animation
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                for (i in 0 until 4) {
                    val dotColor by animateColorAsState(
                        targetValue = if (i == activeDotIndex) GoldAccent else TextGray.copy(alpha = 0.5f),
                        animationSpec = tween(durationMillis = 300),
                        label = "dotColor"
                    )
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(dotColor))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Memuat aplikasi...",
                color = Color(0xFFC4C4CD),
                fontSize = 12.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSplashScreen() {
    SplashScreen(onNavigateNext = {})
}
