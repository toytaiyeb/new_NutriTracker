package com.example.nutritrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.nutritrack.presentation.navigation.graph.AppNavGraph
import com.example.nutritrack.presentation.navigation.route.Screen
import com.example.nutritrack.presentation.theme.NutriTrackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NutriTrackTheme {

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
//                        .windowInsetsPadding(WindowInsets.systemBars)
                ) { innerPadding ->
                    val navController = rememberNavController()
                    AppNavGraph(
                        navController = navController,
                        startDestination = Screen.Welcome.route,
                    )
                }
            }
        }
    }
}
