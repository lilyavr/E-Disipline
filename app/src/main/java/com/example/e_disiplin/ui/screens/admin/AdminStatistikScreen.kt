package com.example.e_disiplin.ui.screens.admin

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.e_disiplin.util.ExportManager
import kotlin.math.cos
import kotlin.math.sin

// ─── Design Tokens ───────────────────────────────────────────────────────────
private val BgGradientStart = Color(0xFF0D1B6E)
private val BgGradientEnd   = Color(0xFF1B3FA8)
private val CardBg          = Color(0xFFF4F6FC)
private val TextNavy        = Color(0xFF0D1B6E)
private val TextGray        = Color(0xFF8A8D9E)
private val AccentGold      = Color(0xFFF2B705)
private val AccentTeal      = Color(0xFF00C9A7)
private val AccentPink      = Color(0xFFFF6B9D)
private val AccentOrange    = Color(0xFFFF8C42)

private val ChartPalette = listOf(
    Color(0xFF4361EE),
    Color(0xFF7209B7),
    Color(0xFF00C9A7),
    Color(0xFFFF8C42),
    Color(0xFFFF6B9D),
    Color(0xFFF2B705)
)

// ─── Screen ──────────────────────────────────────────────────────────────────
@Composable
fun AdminStatistikScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: AdminStatistikViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F3FB))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // ── Header ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(BgGradientStart, BgGradientEnd)
                        ),
                        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                    )
                    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal))
                    .padding(start = 8.dp, end = 24.dp, bottom = 28.dp, top = 8.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Kembali",
                                tint = Color.White
                            )
                        }
                        Text(
                            text = "Laporan & Statistik",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Summary pill row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SummaryPill(
                            modifier = Modifier.weight(1f),
                            label = "Total Pelanggaran",
                            value = if (state.isLoading) "..." else state.totalPelanggaran.toString(),
                            accentColor = AccentGold
                        )
                        SummaryPill(
                            modifier = Modifier.weight(1f),
                            label = "Tahun",
                            value = state.selectedYear.toString(),
                            accentColor = AccentTeal
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Year Selector
                    if (state.availableYears.isNotEmpty()) {
                        LazyRow(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(state.availableYears) { year ->
                                val isSelected = year == state.selectedYear
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(
                                            if (isSelected) AccentGold else Color.White.copy(alpha = 0.15f)
                                        )
                                        .border(
                                            1.dp,
                                            if (isSelected) AccentGold else Color.White.copy(alpha = 0.3f),
                                            RoundedCornerShape(20.dp)
                                        )
                                        .clickable { viewModel.selectYear(year) }
                                        .padding(horizontal = 16.dp, vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = year.toString(),
                                        color = if (isSelected) TextNavy else Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = BgGradientEnd)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Memuat data...", color = TextGray, fontSize = 14.sp)
                    }
                }
            } else {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // ── Chart 1: Monthly Trend ──
                    ChartCard(
                        title = "Tren Pelanggaran ${state.selectedYear}",
                        subtitle = "Jumlah per bulan",
                        icon = Icons.AutoMirrored.Filled.TrendingUp,
                        iconTint = BgGradientEnd
                    ) {
                        if (state.monthlyData.all { it.count == 0 }) {
                            EmptyChartPlaceholder("Belum ada data pelanggaran tahun ${state.selectedYear}")
                        } else {
                            MonthlyBarChart(
                                data = state.monthlyData,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            )
                        }
                    }

                    // ── Chart 2: Category Donut ──
                    ChartCard(
                        title = "Pelanggaran per Kategori",
                        subtitle = "Distribusi jenis pelanggaran",
                        icon = Icons.Filled.BarChart,
                        iconTint = Color(0xFF7209B7)
                    ) {
                        if (state.categoryData.isEmpty()) {
                            EmptyChartPlaceholder("Belum ada data kategori")
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                // Donut chart
                                DonutChart(
                                    data = state.categoryData.map { it.percentage },
                                    colors = ChartPalette.take(state.categoryData.size),
                                    modifier = Modifier.size(140.dp)
                                )
                                // Legend
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    state.categoryData.forEachIndexed { idx, item ->
                                        CategoryLegendRow(
                                            color = ChartPalette.getOrElse(idx) { ChartPalette.last() },
                                            label = item.category,
                                            count = item.count,
                                            percentage = item.percentage
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // ── Chart 3: Jurusan Horizontal Bars ──
                    ChartCard(
                        title = "Pelanggaran per Jurusan",
                        subtitle = "Program studi dengan pelanggaran terbanyak",
                        icon = Icons.Filled.BarChart,
                        iconTint = AccentTeal
                    ) {
                        if (state.jurusanData.isEmpty()) {
                            EmptyChartPlaceholder("Belum ada data jurusan")
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                state.jurusanData.forEachIndexed { idx, item ->
                                    JurusanBarRow(
                                        rank = idx + 1,
                                        color = ChartPalette.getOrElse(idx) { ChartPalette.last() },
                                        label = item.jurusan,
                                        count = item.count,
                                        percentage = item.percentage
                                    )
                                }
                            }
                        }
                    }

                    // ── Export Buttons ──
                    ExportButtonRow(
                        isLoading = state.isLoading,
                        onExportPdf = {
                            val intent = ExportManager(context).exportToPdf(
                                year = state.selectedYear,
                                total = state.totalPelanggaran,
                                monthlyData = state.monthlyData,
                                categoryData = state.categoryData,
                                jurusanData = state.jurusanData
                            )
                            if (intent != null) {
                                context.startActivity(
                                    android.content.Intent.createChooser(intent, "Bagikan PDF")
                                )
                            }
                        },
                        onExportExcel = {
                            val intent = ExportManager(context).exportToCsv(
                                year = state.selectedYear,
                                total = state.totalPelanggaran,
                                monthlyData = state.monthlyData,
                                categoryData = state.categoryData,
                                jurusanData = state.jurusanData
                            )
                            if (intent != null) {
                                context.startActivity(
                                    android.content.Intent.createChooser(intent, "Bagikan Excel")
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

// ─── Export Button Row ────────────────────────────────────────────────────────
@Composable
private fun ExportButtonRow(
    isLoading: Boolean,
    onExportPdf: () -> Unit,
    onExportExcel: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Ekspor Laporan",
                color = TextNavy,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Unduh laporan dalam format pilihan",
                color = TextGray,
                fontSize = 11.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // PDF Button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            if (isLoading) Brush.horizontalGradient(listOf(Color(0xFFB0B8D0), Color(0xFF9AA3BC)))
                            else Brush.horizontalGradient(listOf(Color(0xFFE95B5B), Color(0xFFD63737)))
                        )
                        .clickable(enabled = !isLoading) { onExportPdf() },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("📄", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Cetak PDF",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                // Excel Button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            if (isLoading) Brush.horizontalGradient(listOf(Color(0xFFB0B8D0), Color(0xFF9AA3BC)))
                            else Brush.horizontalGradient(listOf(Color(0xFF217346), Color(0xFF1A5C38)))
                        )
                        .clickable(enabled = !isLoading) { onExportExcel() },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("📊", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Export Excel",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// ─── Summary Pill ─────────────────────────────────────────────────────────────
@Composable
private fun SummaryPill(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    accentColor: Color
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.12f))
            .border(1.dp, accentColor.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
            .padding(vertical = 14.dp, horizontal = 16.dp)
    ) {
        Column {
            Text(
                text = value,
                color = accentColor,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 11.sp
            )
        }
    }
}

// ─── Chart Card ──────────────────────────────────────────────────────────────
@Composable
private fun ChartCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(iconTint.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(22.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        color = TextNavy,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = subtitle,
                        color = TextGray,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            content()
        }
    }
}

// ─── Monthly Bar Chart ────────────────────────────────────────────────────────
@Composable
private fun MonthlyBarChart(
    data: List<MonthlyChartData>,
    modifier: Modifier = Modifier
) {
    val animProgress = remember { Animatable(0f) }
    LaunchedEffect(data) {
        animProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        )
    }

    val maxValue = data.maxOfOrNull { it.count }?.coerceAtLeast(1) ?: 1
    val barGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF4361EE), Color(0xFF7B2FBE))
    )
    val barGradientHighlight = Brush.verticalGradient(
        colors = listOf(AccentGold, AccentOrange)
    )
    val currentMonth = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH)
    val progress = animProgress.value

    Canvas(modifier = modifier) {
        val chartWidth = size.width
        val chartHeight = size.height
        val bottomPadding = 40f
        val topPadding = 20f
        val availableHeight = chartHeight - bottomPadding - topPadding
        val barCount = data.size
        val totalBarWidth = chartWidth / barCount
        val barWidth = totalBarWidth * 0.55f
        val cornerRadius = barWidth / 4

        // Horizontal grid lines
        val gridCount = 4
        for (i in 0..gridCount) {
            val y = topPadding + availableHeight * (1f - i.toFloat() / gridCount)
            drawLine(
                color = Color(0xFFE8ECF4),
                start = Offset(0f, y),
                end = Offset(chartWidth, y),
                strokeWidth = 1f
            )
            if (i > 0) {
                val gridValue = (maxValue * i / gridCount)
                drawContext.canvas.nativeCanvas.apply {
                    val paint = android.graphics.Paint().apply {
                        color = TextGray.toArgb()
                        textSize = 24f
                        textAlign = android.graphics.Paint.Align.RIGHT
                    }
                    drawText(gridValue.toString(), 56f, y + 8f, paint)
                }
            }
        }

        data.forEachIndexed { index, monthData ->
            val barHeightFraction = (monthData.count.toFloat() / maxValue) * progress
            val barH = availableHeight * barHeightFraction
            val barLeft = totalBarWidth * index + (totalBarWidth - barWidth) / 2
            val barTop = topPadding + availableHeight - barH

            val isCurrentMonth = index == currentMonth
            val brush = if (isCurrentMonth) barGradientHighlight else barGradient

            // Draw bar with rounded top corners
            if (barH > cornerRadius) {
                drawRoundRect(
                    brush = brush,
                    topLeft = Offset(barLeft, barTop),
                    size = Size(barWidth, barH),
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                )
            } else if (barH > 0) {
                drawRect(
                    brush = brush,
                    topLeft = Offset(barLeft, barTop),
                    size = Size(barWidth, barH)
                )
            }

            // Month label
            drawContext.canvas.nativeCanvas.apply {
                val paint = android.graphics.Paint().apply {
                    color = if (isCurrentMonth) AccentGold.toArgb() else TextGray.toArgb()
                    textSize = 26f
                    textAlign = android.graphics.Paint.Align.CENTER
                    isFakeBoldText = isCurrentMonth
                }
                drawText(monthData.month, barLeft + barWidth / 2, chartHeight - 8f, paint)
            }

            // Value label on top of bar
            if (monthData.count > 0 && progress == 1f) {
                drawContext.canvas.nativeCanvas.apply {
                    val paint = android.graphics.Paint().apply {
                        color = TextNavy.toArgb()
                        textSize = 22f
                        textAlign = android.graphics.Paint.Align.CENTER
                        isFakeBoldText = true
                    }
                    drawText(
                        monthData.count.toString(),
                        barLeft + barWidth / 2,
                        barTop - 6f,
                        paint
                    )
                }
            }
        }
    }
}

// ─── Donut Chart ─────────────────────────────────────────────────────────────
@Composable
private fun DonutChart(
    data: List<Float>,
    colors: List<Color>,
    modifier: Modifier = Modifier
) {
    val animProgress = remember { Animatable(0f) }
    LaunchedEffect(data) {
        animProgress.snapTo(0f)
        animProgress.animateTo(1f, tween(900, easing = FastOutSlowInEasing))
    }
    val progress = animProgress.value
    val totalItems = data.size

    Canvas(modifier = modifier) {
        val diameter = size.minDimension
        val strokeWidth = diameter * 0.18f
        val radius = (diameter - strokeWidth) / 2f
        val center = Offset(size.width / 2f, size.height / 2f)
        var startAngle = -90f

        data.forEachIndexed { i, fraction ->
            val sweep = fraction * 360f * progress
            drawArc(
                color = colors.getOrElse(i) { Color.Gray },
                startAngle = startAngle,
                sweepAngle = sweep,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
            )
            startAngle += fraction * 360f

            // Gap between segments
            startAngle += 1f
        }

        // Center circle with count
        drawCircle(color = Color(0xFFF4F6FC), radius = radius - strokeWidth / 2 - 4f, center = center)

        drawContext.canvas.nativeCanvas.apply {
            val countPaint = android.graphics.Paint().apply {
                color = TextNavy.toArgb()
                textSize = diameter * 0.14f
                textAlign = android.graphics.Paint.Align.CENTER
                isFakeBoldText = true
            }
            val labelPaint = android.graphics.Paint().apply {
                color = TextGray.toArgb()
                textSize = diameter * 0.09f
                textAlign = android.graphics.Paint.Align.CENTER
            }
            drawText(totalItems.toString(), center.x, center.y + 8f, countPaint)
            drawText("kategori", center.x, center.y + diameter * 0.13f, labelPaint)
        }
    }
}

// ─── Category Legend Row ─────────────────────────────────────────────────────
@Composable
private fun CategoryLegendRow(
    color: Color,
    label: String,
    count: Int,
    percentage: Float
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                color = TextNavy,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "$count kasus • ${(percentage * 100).toInt()}%",
                color = TextGray,
                fontSize = 10.sp
            )
        }
    }
}

// ─── Jurusan Bar Row ─────────────────────────────────────────────────────────
@Composable
private fun JurusanBarRow(
    rank: Int,
    color: Color,
    label: String,
    count: Int,
    percentage: Float
) {
    val animProgress = remember(label) { Animatable(0f) }
    LaunchedEffect(percentage) {
        animProgress.snapTo(0f)
        animProgress.animateTo(
            percentage,
            animationSpec = tween(700, delayMillis = rank * 80, easing = FastOutSlowInEasing)
        )
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank badge
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = rank.toString(),
                    color = color,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = label,
                color = TextNavy,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "$count",
                color = color,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Progress bar track
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(50))
                .background(color.copy(alpha = 0.1f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animProgress.value)
                    .height(8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(color, color.copy(alpha = 0.6f))
                        )
                    )
            )
        }
    }
}

// ─── Empty Placeholder ────────────────────────────────────────────────────────
@Composable
private fun EmptyChartPlaceholder(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("📊", fontSize = 32.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                color = TextGray,
                fontSize = 13.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAdminStatistikScreen() {
    AdminStatistikScreen()
}
