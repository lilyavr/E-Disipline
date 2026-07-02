package com.example.e_disiplin.ui.screens.admin

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.e_disiplin.ui.components.CustomListIcon

@Composable
fun AdminMainScreen(
    onLogoutClick: () -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "beranda"

    // Shared ViewModel across all admin screens that need mahasiswa data
    val mahasiswaViewModel: AdminMahasiswaViewModel = viewModel()

    // Only show bottom bar on the main tab screens (not sub-screens)
    val showBottomBar = currentRoute in listOf("beranda", "data", "notifikasi", "profil")

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                AdminBottomNavigationBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "beranda",
            modifier = Modifier
                .padding(bottom = innerPadding.calculateBottomPadding())
                .fillMaxSize()
        ) {
            composable("beranda") {
                AdminDashboardScreen(viewModel = mahasiswaViewModel)
            }
            composable("data") {
                AdminDataScreen()
            }
            composable("kategori") {
                AdminKategoriScreen()
            }
            composable("pengaturan_sistem") {
                AdminSettingsScreen(
                    onNavigateToKategori = { navController.navigate("kategori") }
                )
            }
            composable("notifikasi") {
                AdminNotificationScreen()
            }
            composable("profil") {
                AdminProfileScreen(
                    onLogoutClick = onLogoutClick,
                    onNavigateToSettings = { navController.navigate("pengaturan_sistem") },
                    onNavigateToMahasiswa = { navController.navigate("daftar_mahasiswa") },
                    onNavigateToPanduan = { navController.navigate("panduan") }
                )
            }
            composable("panduan") {
                AdminPanduanScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable("daftar_mahasiswa") {
                AdminDaftarMahasiswaScreen(
                    viewModel = mahasiswaViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onMahasiswaClick = { mahasiswa ->
                        mahasiswaViewModel.selectMahasiswa(mahasiswa)
                        navController.navigate("mahasiswa_detail")
                    },
                    onAddMahasiswaClick = { navController.navigate("tambah_mahasiswa") }
                )
            }
            composable("tambah_mahasiswa") {
                AdminAddMahasiswaScreen(
                    viewModel = mahasiswaViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable("mahasiswa_detail") {
                AdminMahasiswaDetailScreen(
                    viewModel = mahasiswaViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
fun AdminBottomNavigationBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val textNavy = Color(0xFF1B2154)
    val textGray = Color(0xFF8A8D9E)

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.windowInsetsPadding(
            WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal)
        )
    ) {
        NavigationBarItem(
            selected = currentRoute == "beranda",
            onClick = { onNavigate("beranda") },
            icon = { Icon(Icons.Filled.Home, contentDescription = "Beranda") },
            label = {
                Text(
                    "Beranda",
                    fontSize = 10.sp,
                    fontWeight = if (currentRoute == "beranda") FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = textNavy, selectedTextColor = textNavy,
                unselectedIconColor = textGray, unselectedTextColor = textGray,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = currentRoute == "data",
            onClick = { onNavigate("data") },
            icon = { CustomListIcon(tint = if (currentRoute == "data") textNavy else textGray) },
            label = {
                Text(
                    "Data",
                    fontSize = 10.sp,
                    fontWeight = if (currentRoute == "data") FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = textNavy, selectedTextColor = textNavy,
                unselectedIconColor = textGray, unselectedTextColor = textGray,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = currentRoute == "notifikasi",
            onClick = { onNavigate("notifikasi") },
            icon = { Icon(Icons.Outlined.Notifications, contentDescription = "Notifikasi") },
            label = {
                Text(
                    "Notifikasi",
                    fontSize = 10.sp,
                    fontWeight = if (currentRoute == "notifikasi") FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = textNavy, selectedTextColor = textNavy,
                unselectedIconColor = textGray, unselectedTextColor = textGray,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = currentRoute == "profil",
            onClick = { onNavigate("profil") },
            icon = { Icon(Icons.Outlined.Person, contentDescription = "Profil") },
            label = {
                Text(
                    "Profil",
                    fontSize = 10.sp,
                    fontWeight = if (currentRoute == "profil") FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = textNavy, selectedTextColor = textNavy,
                unselectedIconColor = textGray, unselectedTextColor = textGray,
                indicatorColor = Color.Transparent
            )
        )
    }
}
