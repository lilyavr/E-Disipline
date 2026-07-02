package com.example.e_disiplin.ui.screens.mahasiswa

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.example.e_disiplin.ui.components.CustomListIcon
import com.example.e_disiplin.ui.screens.mahasiswa.MahasiswaNotificationScreen
import com.example.e_disiplin.ui.screens.mahasiswa.MahasiswaProfileScreen
import com.example.e_disiplin.ui.screens.mahasiswa.MahasiswaRiwayatScreen
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.runtime.collectAsState

@Composable
fun MahasiswaMainScreen(
    nim: String,
    onLogoutClick: () -> Unit,
    viewModel: MahasiswaDashboardViewModel = viewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "beranda"

    val scanLauncher = rememberLauncherForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            viewModel.processScan(result.contents, nim) { 
                navController.navigate("scan_report")
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val options = ScanOptions()
                    options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                    options.setPrompt("Arahkan kamera ke QR Code")
                    options.setCameraId(0)
                    options.setBeepEnabled(true)
                    options.setBarcodeImageEnabled(false)
                    scanLauncher.launch(options)
                },
                shape = CircleShape,
                containerColor = Color(0xFF1C2E5F),
                contentColor = Color.White,
                modifier = Modifier
                    .offset(y = 56.dp) // Push it down to be exactly in the middle of the navbar
                    .size(64.dp) // Make it prominent
            ) {
                // Custom Scan Icon
                androidx.compose.foundation.Canvas(modifier = Modifier.size(28.dp)) {
                    val strokeWidth = 2.dp.toPx()
                    // Top-left
                    drawLine(Color.White, androidx.compose.ui.geometry.Offset(0f, 8.dp.toPx()), androidx.compose.ui.geometry.Offset(0f, 0f), strokeWidth)
                    drawLine(Color.White, androidx.compose.ui.geometry.Offset(0f, 0f), androidx.compose.ui.geometry.Offset(8.dp.toPx(), 0f), strokeWidth)
                    // Top-right
                    drawLine(Color.White, androidx.compose.ui.geometry.Offset(size.width - 8.dp.toPx(), 0f), androidx.compose.ui.geometry.Offset(size.width, 0f), strokeWidth)
                    drawLine(Color.White, androidx.compose.ui.geometry.Offset(size.width, 0f), androidx.compose.ui.geometry.Offset(size.width, 8.dp.toPx()), strokeWidth)
                    // Bottom-left
                    drawLine(Color.White, androidx.compose.ui.geometry.Offset(0f, size.height - 8.dp.toPx()), androidx.compose.ui.geometry.Offset(0f, size.height), strokeWidth)
                    drawLine(Color.White, androidx.compose.ui.geometry.Offset(0f, size.height), androidx.compose.ui.geometry.Offset(8.dp.toPx(), size.height), strokeWidth)
                    // Bottom-right
                    drawLine(Color.White, androidx.compose.ui.geometry.Offset(size.width - 8.dp.toPx(), size.height), androidx.compose.ui.geometry.Offset(size.width, size.height), strokeWidth)
                    drawLine(Color.White, androidx.compose.ui.geometry.Offset(size.width, size.height), androidx.compose.ui.geometry.Offset(size.width, size.height - 8.dp.toPx()), strokeWidth)
                    // Center line
                    drawLine(Color.White, androidx.compose.ui.geometry.Offset(2.dp.toPx(), size.height / 2), androidx.compose.ui.geometry.Offset(size.width - 2.dp.toPx(), size.height / 2), strokeWidth)
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            MahasiswaBottomNavigationBar(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    if (route.isNotEmpty()) {
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "beranda",
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()).fillMaxSize()
        ) {
            composable("beranda") {
                MahasiswaDashboardScreen(nim = nim, viewModel = viewModel)
            }
            composable("scan_report") {
                val pelanggaran = viewModel.lastScannedPelanggaran.collectAsState().value
                val mahasiswa = viewModel.mahasiswa.collectAsState().value
                
                if (pelanggaran != null && mahasiswa != null) {
                    val formatter = SimpleDateFormat("dd MMM yyyy | HH:mm 'WIB'", Locale("id", "ID"))
                    val waktu = formatter.format(Date(pelanggaran.tanggal))

                    MahasiswaScanReportScreen(
                        pelanggaranId = pelanggaran.id.take(8).uppercase(), // Short ID
                        jenis = pelanggaran.kategoriName,
                        mahasiswaName = mahasiswa.name,
                        nim = mahasiswa.nim,
                        waktu = waktu,
                        onNavigateBack = {
                            navController.popBackStack("beranda", false)
                        }
                    )
                }
            }
            composable("riwayat") {
                MahasiswaRiwayatScreen()
            }
            composable("notifikasi") {
                MahasiswaNotificationScreen(viewModel = viewModel)
            }
            composable("profil") {
                MahasiswaProfileScreen(onLogoutClick = onLogoutClick)
            }
        }
    }
}

@Composable
fun MahasiswaBottomNavigationBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val textNavy = Color(0xFF1B2154)
    val textGray = Color(0xFF8A8D9E)

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal))
    ) {
        NavigationBarItem(
            selected = currentRoute == "beranda",
            onClick = { onNavigate("beranda") },
            icon = { Icon(Icons.Filled.Home, contentDescription = "Beranda") },
            label = { 
                Text("Beranda", fontSize = 10.sp, fontWeight = if (currentRoute == "beranda") FontWeight.Bold else FontWeight.Normal) 
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = textNavy, selectedTextColor = textNavy, unselectedIconColor = textGray, unselectedTextColor = textGray, indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = currentRoute == "riwayat",
            onClick = { onNavigate("riwayat") },
            icon = { CustomListIcon(tint = if (currentRoute == "riwayat") textNavy else textGray) },
            label = { 
                Text("Riwayat", fontSize = 10.sp, fontWeight = if (currentRoute == "riwayat") FontWeight.Bold else FontWeight.Normal) 
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = textNavy, selectedTextColor = textNavy, unselectedIconColor = textGray, unselectedTextColor = textGray, indicatorColor = Color.Transparent
            )
        )
        
        // Empty item for the floating action button to sit in the middle
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { },
            label = { },
            enabled = false,
            colors = NavigationBarItemDefaults.colors(
                disabledIconColor = Color.Transparent, disabledTextColor = Color.Transparent, indicatorColor = Color.Transparent
            )
        )
        
        NavigationBarItem(
            selected = currentRoute == "notifikasi",
            onClick = { onNavigate("notifikasi") },
            icon = { Icon(Icons.Outlined.Notifications, contentDescription = "Notifikasi") },
            label = { 
                Text("Notifikasi", fontSize = 10.sp, fontWeight = if (currentRoute == "notifikasi") FontWeight.Bold else FontWeight.Normal) 
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = textNavy, selectedTextColor = textNavy, unselectedIconColor = textGray, unselectedTextColor = textGray, indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = currentRoute == "profil",
            onClick = { onNavigate("profil") },
            icon = { Icon(Icons.Outlined.Person, contentDescription = "Profil") },
            label = { 
                Text("Profil", fontSize = 10.sp, fontWeight = if (currentRoute == "profil") FontWeight.Bold else FontWeight.Normal) 
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = textNavy, selectedTextColor = textNavy, unselectedIconColor = textGray, unselectedTextColor = textGray, indicatorColor = Color.Transparent
            )
        )
    }
}
