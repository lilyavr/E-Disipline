package com.example.e_disiplin.ui.screens.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.clickable

// Colors matching the design
private val BgCream = Color(0xFFF9F9F4)
private val TextNavy = Color(0xFF1B2154)
private val TextGray = Color(0xFF8A8D9E)
private val ButtonActiveBg = Color(0xFF2A3F7B)
private val CardBorder = Color(0xFFE5E5EA)
private val TagBg = Color(0xFFF3F3F6)
private val GreenTagBg = Color(0xFFE6F4EA)
private val GreenTagText = Color(0xFF34A853)
private val YellowTagBg = Color(0xFFFFF7E6)
private val YellowTagText = Color(0xFFD49A00)
private val PointsRed = Color(0xFFE95B5B)

data class ViolationData(
    val name: String,
    val nim: String,
    val description: String,
    val date: String,
    val status: String,
    val points: String,
    val timestamp: Long = 0L
)

@Composable
fun AdminDataScreen(
    viewModel: AdminDataPelanggaranViewModel = viewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Semua") }
    
    val allViolations by viewModel.allViolations.collectAsState()
    
    val currentMonthYear = remember {
        java.util.Calendar.getInstance().let {
            Pair(it.get(java.util.Calendar.MONTH), it.get(java.util.Calendar.YEAR))
        }
    }
    
    val filteredViolations = allViolations.filter { v ->
        val matchesSearch = v.name.contains(searchQuery, ignoreCase = true) || 
                            v.nim.contains(searchQuery, ignoreCase = true)
                            
        val matchesFilter = if (selectedFilter == "Bulan Ini") {
            val cal = java.util.Calendar.getInstance().apply { timeInMillis = v.timestamp }
            cal.get(java.util.Calendar.MONTH) == currentMonthYear.first && 
            cal.get(java.util.Calendar.YEAR) == currentMonthYear.second
        } else true
        
        matchesSearch && matchesFilter
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgCream)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            
            // Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp)
            ) {
                Text(
                    text = "Data Pelanggaran",
                    color = TextNavy,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Semua catatan pelanggaran mahasiswa",
                    color = TextGray,
                    fontSize = 14.sp
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(text = "Cari nama atau NIM...", color = TextGray)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search Icon",
                            tint = TextNavy
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
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
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FilterChip(
                        text = "Semua", 
                        isActive = selectedFilter == "Semua",
                        onClick = { selectedFilter = "Semua" }
                    )
                    FilterChip(
                        text = "Bulan Ini", 
                        isActive = selectedFilter == "Bulan Ini",
                        onClick = { selectedFilter = "Bulan Ini" }
                    )
                }
            }
            
            // List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(
                    start = 24.dp, 
                    end = 24.dp, 
                    bottom = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (filteredViolations.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            Text("Tidak ada data", color = TextGray)
                        }
                    }
                } else {
                    items(filteredViolations) { data ->
                        ViolationCard(data)
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChip(text: String, isActive: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clickable { onClick() }
            .background(
                color = if (isActive) ButtonActiveBg else Color.White,
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                width = 1.dp,
                color = if (isActive) Color.Transparent else CardBorder,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 20.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isActive) Color.White else TextGray,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun ViolationCard(data: ViolationData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(TagBg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🎓", fontSize = 24.sp)
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Name and Description
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = data.name,
                        color = TextNavy,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = data.nim,
                        color = TextGray,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = data.description,
                        color = TextNavy.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                }
                
                // Status and Points
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    val isVerified = data.status == "Verified"
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (isVerified) GreenTagBg else YellowTagBg,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = if (isVerified) GreenTagText.copy(alpha = 0.3f) else YellowTagText.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = data.status,
                            color = if (isVerified) GreenTagText else YellowTagText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    Text(
                        text = data.points,
                        color = PointsRed,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = CardBorder, thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))
            
            // Date
            Text(
                text = data.date,
                color = TextGray,
                fontSize = 12.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAdminDataScreen() {
    AdminDataScreen()
}
