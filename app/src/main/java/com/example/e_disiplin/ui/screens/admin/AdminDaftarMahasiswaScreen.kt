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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.e_disiplin.R
import com.example.e_disiplin.domain.model.Mahasiswa

private val DMLNavy  = Color(0xFF1B2154)
private val DMLAccent = Color(0xFF2A3F7B)
private val DMLGold  = Color(0xFFF2B705)
private val DMLGray  = Color(0xFF8A8D9E)
private val DMLGreen = Color(0xFF4CAF50)
private val DMLRed   = Color(0xFFE95B5B)
private val DMLBg    = Color(0xFFF0F3FB)
private val DMLCard  = Color(0xFFFFFFFF)

@Composable
fun AdminDaftarMahasiswaScreen(
    viewModel: AdminMahasiswaViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    onMahasiswaClick: (Mahasiswa) -> Unit,
    onAddMahasiswaClick: () -> Unit
) {
    val mahasiswaList by viewModel.mahasiswaList.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val filtered = mahasiswaList.filter { m ->
        searchQuery.isBlank() ||
            m.name.contains(searchQuery, ignoreCase = true) ||
            m.nim.contains(searchQuery, ignoreCase = true) ||
            m.major.contains(searchQuery, ignoreCase = true)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DMLBg)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── Header ────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bg_main_hero),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(
                            WindowInsets.safeDrawing.only(
                                WindowInsetsSides.Top + WindowInsetsSides.Horizontal
                            )
                        )
                        .padding(24.dp)
                ) {
                    // Top bar row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onNavigateBack,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f))
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Kembali",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Daftar Mahasiswa",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${mahasiswaList.size} mahasiswa terdaftar",
                                color = Color.White.copy(alpha = 0.65f),
                                fontSize = 12.sp
                            )
                        }
                        Button(
                            onClick = { onAddMahasiswaClick() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DMLGold,
                                contentColor = DMLNavy
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("+ Tambah", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Search bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Cari nama, NIM, atau prodi…", fontSize = 13.sp) },
                        leadingIcon = {
                            Icon(Icons.Filled.Search, contentDescription = null, tint = Color.White.copy(alpha = 0.7f))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White.copy(alpha = 0.12f),
                            unfocusedContainerColor = Color.White.copy(alpha = 0.10f),
                            focusedBorderColor = Color.White.copy(alpha = 0.4f),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = DMLGold,
                            focusedPlaceholderColor = Color.White.copy(alpha = 0.5f),
                            unfocusedPlaceholderColor = Color.White.copy(alpha = 0.4f)
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // ── List ──────────────────────────────────────────────────
            if (filtered.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🎓", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (searchQuery.isBlank()) "Belum ada mahasiswa terdaftar"
                                   else "Tidak ada hasil untuk \"$searchQuery\"",
                            color = DMLGray,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        if (searchQuery.isBlank()) {
                            Text(
                                text = "Tap \"+ Tambah\" untuk mendaftarkan mahasiswa baru.",
                                color = DMLGray,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item { Spacer(modifier = Modifier.height(12.dp)) }

                    items(filtered, key = { it.nim }) { mahasiswa ->
                        MahasiswaListCard(
                            mahasiswa = mahasiswa,
                            onClick = { onMahasiswaClick(mahasiswa) }
                        )
                    }

                    item { Spacer(modifier = Modifier.height(24.dp)) }
                }
            }
        }
    }
}

@Composable
fun MahasiswaListCard(
    mahasiswa: Mahasiswa,
    onClick: () -> Unit
) {
    val poinColor = when {
        mahasiswa.totalPoin == 0 -> DMLGreen
        mahasiswa.totalPoin < 30 -> DMLGold
        else -> DMLRed
    }
    val poinBg = when {
        mahasiswa.totalPoin == 0 -> Color(0xFFE8F5E9)
        mahasiswa.totalPoin < 30 -> Color(0xFFFFFDE7)
        else -> Color(0xFFFDECEB)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = DMLCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar circle
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.verticalGradient(listOf(Color(0xFF2A3F7B), Color(0xFF3D5A99)))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = mahasiswa.name,
                    color = DMLNavy,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "NIM: ${mahasiswa.nim}",
                    color = DMLGray,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${mahasiswa.major} • Sem. ${mahasiswa.semester}",
                    color = DMLGray,
                    fontSize = 12.sp,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Poin badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(poinBg)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (mahasiswa.totalPoin == 0) "0 Poin • Baik"
                               else "${mahasiswa.totalPoin} Poin",
                        color = poinColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Detail",
                tint = DMLGray,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

// ── Preview helpers ────────────────────────────────────────────────────────────

private val sampleMahasiswaList = listOf(
    Mahasiswa(
        nim = "2021133001", name = "Andi Pratama",
        email = "andi@student.uvers.ac.id", noHp = "081234567890",
        major = "Teknik Informatika", semester = "5",
        tanggalLahir = "01012000", totalPoin = 0
    ),
    Mahasiswa(
        nim = "2021133002", name = "Budi Santoso",
        email = "budi@student.uvers.ac.id", noHp = "081234567891",
        major = "Manajemen", semester = "3",
        tanggalLahir = "15031999", totalPoin = 15
    ),
    Mahasiswa(
        nim = "2021133003", name = "Citra Dewi",
        email = "citra@student.uvers.ac.id", noHp = "081234567892",
        major = "Akuntansi", semester = "7",
        tanggalLahir = "20061998", totalPoin = 45
    ),
    Mahasiswa(
        nim = "2021133004", name = "Dina Rahayu",
        email = "dina@student.uvers.ac.id", noHp = "081234567893",
        major = "Teknik Informatika", semester = "1",
        tanggalLahir = "07092002", totalPoin = 0
    )
)

/**
 * Full-screen preview of the Daftar Mahasiswa page.
 * Uses static sample data — no ViewModel or Firebase needed.
 */
@Preview(showBackground = true, showSystemUi = true, name = "Daftar Mahasiswa – Full Screen")
@Composable
fun PreviewAdminDaftarMahasiswaScreen() {
    var searchQuery by remember { mutableStateOf("") }

    val filtered = sampleMahasiswaList.filter { m ->
        searchQuery.isBlank() ||
            m.name.contains(searchQuery, ignoreCase = true) ||
            m.nim.contains(searchQuery, ignoreCase = true) ||
            m.major.contains(searchQuery, ignoreCase = true)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DMLBg)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Simulated header (gradient box without real hero image)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                    .background(
                        androidx.compose.ui.graphics.Brush.verticalGradient(
                            listOf(Color(0xFF1B2154), Color(0xFF2A3F7B))
                        )
                    )
                    .padding(24.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Back button placeholder
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Daftar Mahasiswa",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${sampleMahasiswaList.size} mahasiswa terdaftar",
                                color = Color.White.copy(alpha = 0.65f),
                                fontSize = 12.sp
                            )
                        }
                        Button(
                            onClick = { },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DMLGold,
                                contentColor = DMLNavy
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("+ Tambah", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Cari nama, NIM, atau prodi…", fontSize = 13.sp) },
                        leadingIcon = {
                            Icon(
                                Icons.Filled.Search,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.7f)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White.copy(alpha = 0.12f),
                            unfocusedContainerColor = Color.White.copy(alpha = 0.10f),
                            focusedBorderColor = Color.White.copy(alpha = 0.4f),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = DMLGold,
                            focusedPlaceholderColor = Color.White.copy(alpha = 0.5f),
                            unfocusedPlaceholderColor = Color.White.copy(alpha = 0.4f)
                        ),
                        singleLine = true
                    )
                }
            }

            // Mahasiswa list
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { Spacer(modifier = Modifier.height(12.dp)) }
                items(filtered, key = { it.nim }) { mahasiswa ->
                    MahasiswaListCard(mahasiswa = mahasiswa, onClick = { })
                }
                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }
    }
}

/** Lightweight preview of a single MahasiswaListCard in each poin state */
@Preview(showBackground = true, name = "Kartu Mahasiswa – Baik (0 poin)")
@Composable
fun PreviewMahasiswaCardBaik() {
    MahasiswaListCard(mahasiswa = sampleMahasiswaList[0], onClick = { })
}

@Preview(showBackground = true, name = "Kartu Mahasiswa – Sedang (15 poin)")
@Composable
fun PreviewMahasiswaCardSedang() {
    MahasiswaListCard(mahasiswa = sampleMahasiswaList[1], onClick = { })
}

@Preview(showBackground = true, name = "Kartu Mahasiswa – Tinggi (45 poin)")
@Composable
fun PreviewMahasiswaCardTinggi() {
    MahasiswaListCard(mahasiswa = sampleMahasiswaList[2], onClick = { })
}

