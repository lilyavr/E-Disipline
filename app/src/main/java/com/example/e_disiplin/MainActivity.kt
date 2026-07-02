package com.example.e_disiplin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.e_disiplin.ui.navigation.AppNavigation
import com.example.e_disiplin.ui.theme.EDisiplinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EDisiplinTheme {
                AppNavigation()
            }
        }
    }
}
