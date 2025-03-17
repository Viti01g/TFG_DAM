package com.example.gestioncitasapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.gestioncitasapp.ui.theme.GestionCitasAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GestionCitasAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Llamamos a la pantalla principal MainScreen
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
