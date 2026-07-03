package com.example.e_disiplin.util

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import com.example.e_disiplin.ui.screens.admin.CategoryChartData
import com.example.e_disiplin.ui.screens.admin.JurusanChartData
import com.example.e_disiplin.ui.screens.admin.MonthlyChartData
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExportManager(private val context: Context) {

    private val exportsDir: File
        get() = File(context.cacheDir, "exports").also { it.mkdirs() }

    // ─── Reusable Paint Factories ─────────────────────────────────────────────

    /**
     * Creates a Paint object customized for rendering text.
     * @param color The text color.
     * @param size The text size.
     * @param bold Whether the text should be bold.
     * @return A configured [Paint] instance.
     */
    private fun textPaint(color: Int, size: Float, bold: Boolean = false) = Paint().apply {
        this.color = color
        textSize = size
        isAntiAlias = true
        if (bold) isFakeBoldText = true
    }

    /**
     * Creates a Paint object customized for filling shapes (e.g., rectangles, circles).
     * @param color The fill color.
     * @return A configured [Paint] instance with Fill style.
     */
    private fun fillPaint(color: Int) = Paint().apply {
        this.color = color
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    /**
     * Creates a Paint object customized for drawing strokes (lines, borders).
     * @param color The stroke color.
     * @param width The stroke thickness in pixels.
     * @return A configured [Paint] instance with Stroke style.
     */
    private fun strokePaint(color: Int, width: Float = 1f) = Paint().apply {
        this.color = color
        style = Paint.Style.STROKE
        strokeWidth = width
        isAntiAlias = true
    }

    // ─── CSV (Excel) Export ───────────────────────────────────────────────────

    /**
     * Exports the provided statistics data into a CSV (Comma-Separated Values) format
     * and returns an Intent to share the generated file.
     * @param year The year of the report.
     * @param total Total number of violations.
     * @param monthlyData List of violations grouped by month.
     * @param categoryData List of violations grouped by category.
     * @param jurusanData List of violations grouped by department.
     * @return An [Intent] configured with ACTION_SEND to share the CSV file, or null if an error occurs.
     */
    fun exportToCsv(
        year: Int,
        total: Int,
        monthlyData: List<MonthlyChartData>,
        categoryData: List<CategoryChartData>,
        jurusanData: List<JurusanChartData>
    ): Intent? {
        return try {
            val file = File(exportsDir, "laporan_edisiplin_$year.csv")
            val sb = StringBuilder()

            // UTF-8 BOM so Excel opens Indonesian characters correctly
            sb.append('\uFEFF')

            val exportDate = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.forLanguageTag("id-ID")).format(Date())

            sb.appendLine("LAPORAN STATISTIK PELANGGARAN E-DISIPLIN")
            sb.appendLine("Tahun,$year")
            sb.appendLine("Total Pelanggaran,$total")
            sb.appendLine("Tanggal Ekspor,$exportDate")
            sb.appendLine()

            sb.appendLine("=== TREN PELANGGARAN BULANAN ===")
            sb.appendLine("Bulan,Jumlah Pelanggaran")
            monthlyData.forEach { sb.appendLine("${it.month},${it.count}") }
            sb.appendLine()

            sb.appendLine("=== PELANGGARAN PER KATEGORI ===")
            sb.appendLine("Kategori,Jumlah,Persentase")
            categoryData.forEach {
                sb.appendLine("${it.category},${it.count},${String.format("%.1f%%", it.percentage * 100)}")
            }
            sb.appendLine()

            sb.appendLine("=== PELANGGARAN PER JURUSAN ===")
            sb.appendLine("No,Jurusan,Jumlah,Persentase")
            jurusanData.forEachIndexed { idx, it ->
                sb.appendLine("${idx + 1},${it.jurusan},${it.count},${String.format("%.1f%%", it.percentage * 100)}")
            }

            file.writeText(sb.toString(), Charsets.UTF_8)

            val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
            Intent(Intent.ACTION_SEND).apply {
                type = "text/csv"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "Laporan E-DISIPLIN $year")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // ─── PDF Export ───────────────────────────────────────────────────────────

    /**
     * Exports the provided statistics data into a formatted, multi-page PDF document
     * and returns an Intent to share the generated file.
     * This draws charts, tables, and headers explicitly onto a PDF Canvas.
     * @param year The year of the report.
     * @param total Total number of violations.
     * @param monthlyData List of violations grouped by month.
     * @param categoryData List of violations grouped by category.
     * @param jurusanData List of violations grouped by department.
     * @return An [Intent] configured with ACTION_SEND to share the PDF file, or null if an error occurs.
     */
    fun exportToPdf(
        year: Int,
        total: Int,
        monthlyData: List<MonthlyChartData>,
        categoryData: List<CategoryChartData>,
        jurusanData: List<JurusanChartData>
    ): Intent? {
        return try {
            val pageWidth = 595
            val pageHeight = 842
            val margin = 40f

            val navyColor   = Color.parseColor("#0D1B6E")
            val blueColor   = Color.parseColor("#1B3FA8")
            val tealColor   = Color.parseColor("#00C9A7")
            val goldColor   = Color.parseColor("#F2B705")
            val grayColor   = Color.parseColor("#8A8D9E")
            val lightGray   = Color.parseColor("#F0F3FB")
            val purpleColor = Color.parseColor("#7209B7")
            val chartColors = listOf(
                Color.parseColor("#4361EE"), Color.parseColor("#7209B7"),
                Color.parseColor("#00C9A7"), Color.parseColor("#FF8C42"),
                Color.parseColor("#FF6B9D"), Color.parseColor("#F2B705")
            )
            val exportDate = SimpleDateFormat("dd MMM yyyy", Locale.forLanguageTag("id-ID")).format(Date())

            val document = PdfDocument()

            // ──────────────────────────────────────────────────────────────────
            // PAGE 1 : Cover + Monthly Chart + Monthly Table
            // ──────────────────────────────────────────────────────────────────
            var pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
            var page = document.startPage(pageInfo)
            var canvas: Canvas = page.canvas
            var y: Float

            // Header banner
            canvas.drawRect(0f, 0f, pageWidth.toFloat(), 120f, fillPaint(navyColor))
            canvas.drawRect(0f, 112f, pageWidth.toFloat(), 120f, fillPaint(goldColor))
            canvas.drawText("E-DISIPLIN", margin, 55f, textPaint(Color.WHITE, 28f, bold = true))
            canvas.drawText("Laporan Statistik Pelanggaran", margin, 80f, textPaint(Color.WHITE, 14f))
            canvas.drawText("Tahun $year  •  Total $total Pelanggaran", margin, 105f, textPaint(goldColor, 11f))
            textPaint(Color.WHITE, 9f).also { it.textAlign = Paint.Align.RIGHT }.let {
                canvas.drawText("Diekspor: $exportDate", pageWidth - margin, 55f, it)
            }

            y = 145f

            // Summary cards
            val summaryItems = listOf(
                Triple("Total Pelanggaran", total.toString(), blueColor),
                Triple("Kategori Tercatat", categoryData.size.toString(), purpleColor),
                Triple("Jurusan Terdampak", jurusanData.size.toString(), tealColor),
                Triple("Bulan Aktif", monthlyData.count { it.count > 0 }.toString(), goldColor)
            )
            val cardW = (pageWidth - margin * 2 - 12f * 3) / 4f
            summaryItems.forEachIndexed { idx, (label, value, color) ->
                val x = margin + idx * (cardW + 12f)
                canvas.drawRoundRect(RectF(x, y, x + cardW, y + 65f), 8f, 8f, fillPaint(lightGray))
                canvas.drawRoundRect(RectF(x, y, x + cardW, y + 4f), 2f, 2f, fillPaint(color))
                textPaint(color, 20f, bold = true).also { it.textAlign = Paint.Align.CENTER }.let {
                    canvas.drawText(value, x + cardW / 2f, y + 35f, it)
                }
                textPaint(grayColor, 8f).also { it.textAlign = Paint.Align.CENTER }.let {
                    canvas.drawText(label, x + cardW / 2f, y + 52f, it)
                }
            }
            y += 85f

            // Monthly bar chart
            drawSectionHeader(canvas, "Tren Pelanggaran Bulanan ($year)", margin, y, blueColor, pageWidth)
            y += 30f

            val maxMonthly = monthlyData.maxOfOrNull { it.count }?.coerceAtLeast(1) ?: 1
            val chartHeight = 120f
            val chartWidth = pageWidth - margin * 2
            val barAreaWidth = chartWidth / 12f
            val barW = barAreaWidth * 0.55f

            for (i in 0..4) {
                val lineY = y + chartHeight - (chartHeight * i / 4f)
                canvas.drawLine(margin, lineY, pageWidth - margin, lineY, strokePaint(Color.parseColor("#E8ECF4")))
                if (i > 0) {
                    val gv = (maxMonthly * i / 4)
                    textPaint(grayColor, 7f).also { it.textAlign = Paint.Align.RIGHT }.let { p ->
                        canvas.drawText(gv.toString(), margin - 4f, lineY + 4f, p)
                    }
                }
            }
            monthlyData.forEachIndexed { idx, m ->
                val barH = if (m.count > 0) (chartHeight * m.count.toFloat() / maxMonthly) else 2f
                val bx = margin + idx * barAreaWidth + (barAreaWidth - barW) / 2f
                val by = y + chartHeight - barH
                val barColor = if (m.count == monthlyData.maxOfOrNull { it.count }) goldColor else blueColor
                canvas.drawRoundRect(RectF(bx, by, bx + barW, y + chartHeight), 3f, 3f, fillPaint(barColor))
                textPaint(grayColor, 7f).also { it.textAlign = Paint.Align.CENTER }.let { p ->
                    canvas.drawText(m.month.take(3), bx + barW / 2f, y + chartHeight + 14f, p)
                }
                if (m.count > 0) {
                    textPaint(navyColor, 7f, bold = true).also { it.textAlign = Paint.Align.CENTER }.let { p ->
                        canvas.drawText(m.count.toString(), bx + barW / 2f, by - 3f, p)
                    }
                }
            }
            y += chartHeight + 30f

            // Monthly table
            drawSectionHeader(canvas, "Detail Pelanggaran Per Bulan", margin, y, blueColor, pageWidth)
            y += 28f
            y = drawTable(
                canvas, y, margin, pageWidth,
                headers = listOf("Bulan", "Jumlah Pelanggaran", "Persentase"),
                colWidths = listOf(0.25f, 0.4f, 0.35f),
                rows = monthlyData.map {
                    listOf(it.month, it.count.toString(),
                        String.format("%.1f%%", it.count.toFloat() / total.coerceAtLeast(1) * 100))
                },
                headerColor = blueColor,
                lightGray = lightGray,
                navyColor = navyColor
            )

            document.finishPage(page)

            // ──────────────────────────────────────────────────────────────────
            // PAGE 2 : Category + Jurusan
            // ──────────────────────────────────────────────────────────────────
            pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 2).create()
            page = document.startPage(pageInfo)
            canvas = page.canvas
            y = 0f

            // Mini header
            canvas.drawRect(0f, 0f, pageWidth.toFloat(), 50f, fillPaint(navyColor))
            canvas.drawRect(0f, 46f, pageWidth.toFloat(), 50f, fillPaint(goldColor))
            canvas.drawText("E-DISIPLIN — Laporan $year  (Hal. 2)", margin, 32f, textPaint(Color.WHITE, 12f))
            y = 70f

            // Category section
            drawSectionHeader(canvas, "Pelanggaran Per Kategori", margin, y, purpleColor, pageWidth)
            y += 28f

            if (categoryData.isNotEmpty()) {
                val legendItemH = 28f
                categoryData.forEachIndexed { idx, item ->
                    val color = chartColors.getOrElse(idx) { chartColors.last() }
                    val itemY = y + idx * legendItemH
                    canvas.drawCircle(margin + 6f, itemY + 8f, 5f, fillPaint(color))
                    canvas.drawText(item.category, margin + 18f, itemY + 12f, textPaint(navyColor, 9f))
                    val barStart = margin + 180f
                    val barTotal = pageWidth - margin - barStart - 60f
                    canvas.drawRoundRect(RectF(barStart, itemY + 2f, barStart + barTotal, itemY + 14f), 4f, 4f, fillPaint(Color.parseColor("#EEF0F8")))
                    canvas.drawRoundRect(RectF(barStart, itemY + 2f, barStart + barTotal * item.percentage, itemY + 14f), 4f, 4f, fillPaint(color))
                    textPaint(color, 9f, bold = true).also { it.textAlign = Paint.Align.RIGHT }.let { p ->
                        canvas.drawText(
                            "${item.count} (${String.format("%.1f", item.percentage * 100)}%)",
                            pageWidth - margin, itemY + 12f, p
                        )
                    }
                }
                y += categoryData.size * legendItemH + 12f
            }

            y = drawTable(
                canvas, y, margin, pageWidth,
                headers = listOf("No", "Kategori", "Jumlah", "Persentase"),
                colWidths = listOf(0.08f, 0.52f, 0.2f, 0.2f),
                rows = categoryData.mapIndexed { idx, it ->
                    listOf((idx + 1).toString(), it.category, it.count.toString(),
                        String.format("%.1f%%", it.percentage * 100))
                },
                headerColor = purpleColor,
                lightGray = lightGray,
                navyColor = navyColor
            )
            y += 20f

            // Jurusan section
            drawSectionHeader(canvas, "Pelanggaran Per Jurusan / Program Studi", margin, y, tealColor, pageWidth)
            y += 28f

            if (jurusanData.isNotEmpty()) {
                val legendItemH = 28f
                jurusanData.forEachIndexed { idx, item ->
                    val color = chartColors.getOrElse(idx) { chartColors.last() }
                    val itemY = y + idx * legendItemH
                    canvas.drawCircle(margin + 8f, itemY + 8f, 8f, fillPaint(color))
                    textPaint(Color.WHITE, 7f, bold = true).also { it.textAlign = Paint.Align.CENTER }.let { p ->
                        canvas.drawText((idx + 1).toString(), margin + 8f, itemY + 12f, p)
                    }
                    canvas.drawText(item.jurusan, margin + 22f, itemY + 12f, textPaint(navyColor, 9f))
                    val barStart = margin + 170f
                    val barTotal = pageWidth - margin - barStart - 60f
                    canvas.drawRoundRect(RectF(barStart, itemY + 2f, barStart + barTotal, itemY + 14f), 4f, 4f, fillPaint(Color.parseColor("#EEF0F8")))
                    canvas.drawRoundRect(RectF(barStart, itemY + 2f, barStart + barTotal * item.percentage, itemY + 14f), 4f, 4f, fillPaint(color))
                    textPaint(color, 9f, bold = true).also { it.textAlign = Paint.Align.RIGHT }.let { p ->
                        canvas.drawText(
                            "${item.count} (${String.format("%.1f", item.percentage * 100)}%)",
                            pageWidth - margin, itemY + 12f, p
                        )
                    }
                }
                y += jurusanData.size * legendItemH + 12f
            }

            y = drawTable(
                canvas, y, margin, pageWidth,
                headers = listOf("No", "Jurusan / Program Studi", "Jumlah", "Persentase"),
                colWidths = listOf(0.08f, 0.52f, 0.2f, 0.2f),
                rows = jurusanData.mapIndexed { idx, it ->
                    listOf((idx + 1).toString(), it.jurusan, it.count.toString(),
                        String.format("%.1f%%", it.percentage * 100))
                },
                headerColor = tealColor,
                lightGray = lightGray,
                navyColor = navyColor
            )

            // Footer
            val footerY = pageHeight - 25f
            canvas.drawLine(margin, footerY - 8f, pageWidth - margin, footerY - 8f, strokePaint(Color.parseColor("#E0E0E0")))
            textPaint(grayColor, 8f).also { it.textAlign = Paint.Align.CENTER }.let { p ->
                canvas.drawText(
                    "E-DISIPLIN v1.0 • Universitas Universal • Laporan dibuat otomatis pada $exportDate",
                    pageWidth / 2f, footerY, p
                )
            }

            document.finishPage(page)

            val file = File(exportsDir, "laporan_edisiplin_$year.pdf")
            file.outputStream().use { document.writeTo(it) }
            document.close()

            val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
            Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "Laporan E-DISIPLIN $year")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // ─── Helper: Section Header ───────────────────────────────────────────────

    private fun drawSectionHeader(
        canvas: Canvas,
        title: String,
        x: Float,
        y: Float,
        accentColor: Int,
        pageWidth: Int
    ) {
        canvas.drawRect(x, y, x + 4f, y + 20f, fillPaint(accentColor))
        canvas.drawText(title, x + 10f, y + 15f, textPaint(accentColor, 11f, bold = true))
        canvas.drawLine(
            x + 10f, y + 22f, pageWidth - x, y + 22f,
            strokePaint(Color.parseColor("#E8ECF4"), 0.5f)
        )
    }

    // ─── Helper: Table ────────────────────────────────────────────────────────

    private fun drawTable(
        canvas: Canvas,
        startY: Float,
        margin: Float,
        pageWidth: Int,
        headers: List<String>,
        colWidths: List<Float>,
        rows: List<List<String>>,
        headerColor: Int,
        lightGray: Int,
        navyColor: Int
    ): Float {
        val tableWidth = pageWidth - margin * 2
        val rowH = 22f
        var y = startY

        // Header row
        canvas.drawRect(margin, y, margin + tableWidth, y + rowH, fillPaint(headerColor))
        var colX = margin
        headers.forEachIndexed { i, h ->
            val cw = tableWidth * colWidths[i]
            textPaint(Color.WHITE, 8f, bold = true).also { it.textAlign = Paint.Align.CENTER }.let { p ->
                canvas.drawText(h, colX + cw / 2f, y + 15f, p)
            }
            colX += cw
        }
        y += rowH

        // Data rows
        rows.forEachIndexed { rowIdx, row ->
            val bg = if (rowIdx % 2 == 0) lightGray else Color.WHITE
            canvas.drawRect(margin, y, margin + tableWidth, y + rowH, fillPaint(bg))
            colX = margin
            row.forEachIndexed { i, cell ->
                val cw = tableWidth * colWidths[i]
                val align = if (i == 0) Paint.Align.CENTER else Paint.Align.LEFT
                val textX = if (i == 0) colX + cw / 2f else colX + 4f
                textPaint(navyColor, 8f).also { it.textAlign = align }.let { p ->
                    canvas.drawText(cell, textX, y + 15f, p)
                }
                colX += cw
            }
            canvas.drawLine(margin, y + rowH, margin + tableWidth, y + rowH, strokePaint(Color.parseColor("#E8ECF4"), 0.5f))
            y += rowH
        }

        canvas.drawRect(margin, startY, margin + tableWidth, y, strokePaint(Color.parseColor("#D0D4E8"), 1f))
        return y + 8f
    }
}
