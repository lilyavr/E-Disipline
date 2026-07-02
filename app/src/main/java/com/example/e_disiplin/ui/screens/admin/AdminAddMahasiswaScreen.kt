package com.example.e_disiplin.ui.screens.admin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val BgCream      = Color(0xFFF9F9F4)
private val TextNavy     = Color(0xFF1B2154)
private val TextGray     = Color(0xFF8A8D9E)
private val ButtonActiveBg = Color(0xFF2A3F7B)
private val CardBorder   = Color(0xFFE5E5EA)
private val DropdownBg   = Color(0xFFF5F7FF)

/** Fixed jurusan options */
private val jurusanOptions = listOf(
    "Manajemen",
    "Akuntansi",
    "Teknik Informatika",
    "Sistem Informasi",
    "Teknik Perangkat Lunak",
    "Teknik Industri",
    "Bahasa Mandarin",
    "Pendidikan Bahasa Mandarin",
    "Hukum Bisnis"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminAddMahasiswaScreen(
    viewModel: AdminMahasiswaViewModel,
    onNavigateBack: () -> Unit
) {
    var nim           by remember { mutableStateOf("") }
    var name          by remember { mutableStateOf("") }
    var email         by remember { mutableStateOf("") }
    var noHp          by remember { mutableStateOf("") }
    var major         by remember { mutableStateOf("") }
    var semester      by remember { mutableStateOf("") }
    var tanggalLahir  by remember { mutableStateOf("") }   // stored as DDMMYYYY

    // Date picker dialog visibility
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    // Jurusan dropdown visibility
    var jurusanExpanded by remember { mutableStateOf(false) }

    val addState by viewModel.addState.collectAsState()

    LaunchedEffect(addState) {
        if (addState is AddMahasiswaState.Success) {
            viewModel.resetAddState()
            onNavigateBack()
        }
    }

    // ── Date Picker Dialog ──────────────────────────────────────────────────────
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val fmt = SimpleDateFormat("ddMMyyyy", Locale.forLanguageTag("id-ID"))
                        tanggalLahir = fmt.format(Date(millis))
                    }
                    showDatePicker = false
                }) { Text("OK", color = ButtonActiveBg) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Batal", color = TextGray)
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = Color.White
            )
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    todayContentColor = ButtonActiveBg,
                    todayDateBorderColor = ButtonActiveBg,
                    selectedDayContainerColor = ButtonActiveBg,
                    selectedDayContentColor = Color.White
                )
            )
        }
    }

    // ── Main Screen ─────────────────────────────────────────────────────────────
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
                text = "Tambah Mahasiswa",
                color = TextNavy,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                // NIM
                CustomTextField(value = nim, onValueChange = { nim = it }, label = "NIM")
                Spacer(modifier = Modifier.height(12.dp))

                // Nama
                CustomTextField(value = name, onValueChange = { name = it }, label = "Nama Lengkap")
                Spacer(modifier = Modifier.height(12.dp))

                // Email
                CustomTextField(value = email, onValueChange = { email = it }, label = "Email")
                Spacer(modifier = Modifier.height(12.dp))

                // No HP
                CustomTextField(value = noHp, onValueChange = { noHp = it }, label = "No HP")
                Spacer(modifier = Modifier.height(12.dp))

                // ── Jurusan Dropdown ────────────────────────────────────────
                Text(
                    text = "Program Studi",
                    color = TextGray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Trigger box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = if (jurusanExpanded) ButtonActiveBg else CardBorder,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .clickable { jurusanExpanded = !jurusanExpanded }
                            .padding(horizontal = 16.dp, vertical = 14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (major.isBlank()) "Pilih program studi" else major,
                                color = if (major.isBlank()) TextGray else TextNavy,
                                fontSize = 14.sp
                            )
                            Icon(
                                imageVector = if (jurusanExpanded)
                                    Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                contentDescription = null,
                                tint = TextGray
                            )
                        }
                    }

                    // Dropdown items
                    AnimatedVisibility(
                        visible = jurusanExpanded,
                        enter = expandVertically(),
                        exit = shrinkVertically()
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column {
                                jurusanOptions.forEachIndexed { index, option ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                major = option
                                                jurusanExpanded = false
                                            }
                                            .background(
                                                if (major == option) DropdownBg else Color.White
                                            )
                                            .padding(horizontal = 16.dp, vertical = 12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = option,
                                            color = if (major == option) ButtonActiveBg else TextNavy,
                                            fontSize = 14.sp,
                                            fontWeight = if (major == option) FontWeight.Medium else FontWeight.Normal
                                        )
                                        if (major == option) {
                                            Icon(
                                                imageVector = Icons.Filled.Check,
                                                contentDescription = null,
                                                tint = ButtonActiveBg,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                    if (index < jurusanOptions.lastIndex) {
                                        HorizontalDivider(color = CardBorder, thickness = 0.5.dp)
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Semester
                CustomTextField(value = semester, onValueChange = { semester = it }, label = "Semester")
                Spacer(modifier = Modifier.height(12.dp))

                // ── Tanggal Lahir – tap to open DatePicker ──────────────
                Text(
                    text = "Tanggal Lahir",
                    color = TextGray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .clickable { showDatePicker = true }
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (tanggalLahir.isBlank()) "Pilih tanggal lahir"
                                   else formatDisplayDate(tanggalLahir),
                            color = if (tanggalLahir.isBlank()) TextGray else TextNavy,
                            fontSize = 14.sp
                        )
                        Icon(
                            imageVector = Icons.Filled.CalendarMonth,
                            contentDescription = "Pilih tanggal",
                            tint = ButtonActiveBg,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Password hint
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "🔒 Password otomatis = tanggal lahir (ter-hash)",
                    color = TextGray,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Error message
                if (addState is AddMahasiswaState.Error) {
                    Text(
                        text = (addState as AddMahasiswaState.Error).message,
                        color = Color(0xFFE95B5B),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                
                Spacer(modifier = Modifier.weight(1f))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onNavigateBack,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextNavy),
                        border = BorderStroke(1.dp, CardBorder)
                    ) {
                        Text("Batal")
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = {
                            viewModel.addMahasiswa(nim, name, email, noHp, major, semester, tanggalLahir)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonActiveBg),
                        enabled = addState !is AddMahasiswaState.Loading
                    ) {
                        if (addState is AddMahasiswaState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Simpan")
                        }
                    }
                }
            }
        }
    }
}

// ── Helpers ───────────────────────────────────────────────────────────────────

/**
 * Converts DDMMYYYY to a human-readable string like "01 Januari 2000".
 */
private fun formatDisplayDate(ddmmyyyy: String): String {
    return try {
        val parser = SimpleDateFormat("ddMMyyyy", Locale.forLanguageTag("id-ID"))
        val display = SimpleDateFormat("dd MMMM yyyy", Locale.forLanguageTag("id-ID"))
        display.format(parser.parse(ddmmyyyy)!!)
    } catch (e: Exception) {
        ddmmyyyy
    }
}

@Composable
private fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label, color = TextGray) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = ButtonActiveBg,
            unfocusedBorderColor = CardBorder,
            focusedTextColor = TextNavy,
            unfocusedTextColor = TextNavy
        ),
        singleLine = true
    )
}
