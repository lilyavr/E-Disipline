package com.example.e_disiplin.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.e_disiplin.ui.screens.admin.AdminMainScreen
import com.example.e_disiplin.ui.screens.auth.AdminLoginScreen
import com.example.e_disiplin.ui.screens.auth.MahasiswaLoginScreen
import com.example.e_disiplin.ui.screens.auth.SelectRoleScreen
import com.example.e_disiplin.ui.screens.mahasiswa.MahasiswaMainScreen
import com.example.e_disiplin.ui.screens.onboarding.OnboardingDisciplinScreen
import com.example.e_disiplin.ui.screens.splash.SplashScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(
                onNavigateNext = {
                    navController.navigate("onboarding") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }
        
        composable("onboarding") {
            OnboardingDisciplinScreen(
                onFinishOnboarding = {
                    navController.navigate("select_role") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }
        
        composable("select_role") {
            SelectRoleScreen(
                onRoleSelected = { role ->
                    if (role.equals("Admin", ignoreCase = true)) {
                        navController.navigate("admin_login")
                    } else if (role.equals("Mahasiswa", ignoreCase = true)) {
                        navController.navigate("mahasiswa_login")
                    }
                }
            )
        }
        
        composable("admin_login") {
            AdminLoginScreen(
                onLoginSuccess = {
                    navController.navigate("admin_main") {
                        popUpTo("admin_login") { inclusive = true }
                        popUpTo("select_role") { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable("admin_main") {
            AdminMainScreen(
                onLogoutClick = {
                    navController.navigate("select_role") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        
        composable("mahasiswa_login") {
            MahasiswaLoginScreen(
                onLoginSuccess = { nim ->
                    navController.navigate("mahasiswa_main/$nim") {
                        popUpTo("select_role") { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(
            route = "mahasiswa_main/{nim}",
            arguments = listOf(navArgument("nim") { type = NavType.StringType })
        ) { backStackEntry ->
            val nim = backStackEntry.arguments?.getString("nim") ?: ""
            MahasiswaMainScreen(
                nim = nim,
                onLogoutClick = {
                    navController.navigate("select_role") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
