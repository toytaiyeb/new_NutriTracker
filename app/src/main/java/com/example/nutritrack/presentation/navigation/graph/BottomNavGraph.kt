package com.example.nutritrack.presentation.navigation.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.nutritrack.presentation.navigation.bottomnav.BottomNavItem
import com.example.nutritrack.presentation.navigation.route.Screen
import com.example.nutritrack.presentation.screens.BottomNavigationScreen
import com.example.nutritrack.presentation.screens.main.HomeScreen
import com.example.nutritrack.presentation.screens.main.InsightsScreen
import com.example.nutritrack.presentation.screens.NutriCoachScreen
import com.example.nutritrack.presentation.screens.main.ClinicianDashboardScreen
import com.example.nutritrack.presentation.screens.main.ClinicianLoginScreen
import com.example.nutritrack.presentation.screens.main.LoginScreen
import com.example.nutritrack.presentation.screens.main.SettingsScreen
import com.example.nutritrack.presentation.screens.main.QuestionnaireScreen

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route,
    ) {
        composable(BottomNavItem.Home.route) { HomeScreen(navController) } // ← root navController passed down to home screen
        composable(BottomNavItem.Insights.route) { InsightsScreen(navController) } // ← root navController passed down to insights screen
        composable(BottomNavItem.NutriCoach.route) { NutriCoachScreen(navController) }// ← root navController passed down to nutri coach screen
        composable(BottomNavItem.Settings.route) { SettingsScreen(navController) } //  ← root navController passed down to nutri coach screen
        composable(Screen.Questionnaire.route) { QuestionnaireScreen(navController) } // ← root navController passed down to questionnaire screen
        composable(Screen.BottomNavigation.route) { BottomNavigationScreen(navController) } // ← root navController passed down to bottom navigation screen
        composable(Screen.Login.route) { LoginScreen(navController) } // ← root navController passed down to bottom navigation screen
        composable(BottomNavItem.Clinician.route) { ClinicianLoginScreen(navController) } // ← root navController passed down to bottom navigation screen
        composable(BottomNavItem.ClinicianDashboard.route) { ClinicianDashboardScreen(navController) } // ← root navController passed down to bottom navigation screen

    }
}
