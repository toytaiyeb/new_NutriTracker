package com.example.nutritrack.presentation.navigation.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.nutritrack.presentation.navigation.route.Screen
import com.example.nutritrack.presentation.screens.main.WelcomeScreen
import com.example.nutritrack.presentation.screens.BottomNavigationScreen
import com.example.nutritrack.presentation.screens.main.LoginScreen
import com.example.nutritrack.presentation.screens.main.QuestionnaireScreen
import com.example.nutritrack.presentation.screens.main.RegisterScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(Screen.Welcome.route) { WelcomeScreen(navController) }// passing navController to welcome screen
        composable(Screen.Login.route) { LoginScreen(navController) }   // passing navController to login screen
        composable(Screen.Questionnaire.route) { QuestionnaireScreen(navController) } // passing navController to questionnaire screen
        composable(Screen.BottomNavigation.route) {BottomNavigationScreen(navController)// passing navController to bottom navigation screen
        }
        composable(Screen.Register.route) { RegisterScreen(navController) }
        }
    }

