package com.example.furni

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.furni.navigation.FurniApp
import com.example.furni.ui.theme.FurniTheme
import androidx.lifecycle.lifecycleScope
import com.example.furni.data.database.FurniDatabase
import com.example.furni.data.seed.DatabaseSeeder
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            DatabaseSeeder.seedIfEmpty(FurniDatabase.getInstance(applicationContext))
        }
        enableEdgeToEdge()
        setContent {
            FurniTheme {
                FurniApp()
            }
        }
    }
}