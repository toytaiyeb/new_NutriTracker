package com.example.nutritrack.presentation.screens

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.nutritrack.presentation.navigation.bottomnav.CustomBottomNavigation
import com.example.nutritrack.presentation.navigation.graph.BottomNavGraph
import com.example.nutritrack.presentation.navigation.route.Screen
import com.example.nutritrack.presentation.theme.NutriTrackTheme

@Composable
fun BottomNavigationScreen(navController: NavController) {
    val bottomNavController = rememberNavController()
    val currentRoute = bottomNavController.currentBackStackEntryAsState().value?.destination?.route
    val screensWithoutBottomNav = listOf(Screen.Questionnaire.route,) // hide bottom navigation for these screens

    NutriTrackTheme {
        Scaffold(
            bottomBar = { // bottom navigation bar
                // will show bottom naviagtion only if current screen us  not in that.
                if (currentRoute !in screensWithoutBottomNav) {
                    CustomBottomNavigation(bottomNavController, currentRoute ?: Screen.Home.route)// if not then comeback to home screen

                }
            }
        ) { innerPadding ->// padding for the screen
            BottomNavGraph(bottomNavController)
        }
    }
}
