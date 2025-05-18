package com.example.nutritrack.presentation.navigation.route

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome_screen")
    object Login : Screen("login_screen")
    object Questionnaire : Screen("questionnaire_screen")
    object BottomNavigation : Screen("bottom_nav_screen")

    object Home : Screen("home_screen")
    object Insights : Screen("insights_screen")
    object NutriCoach : Screen("nutri_coach_screen")
    object Settings : Screen("settings_screen")
    object Clinician : Screen("clinician_screen")
    object ClinicianDashboard : Screen("clinician_dashboard_screen")
    object Register : Screen("register_screen/{userId}/{phoneNumber}") {
        fun createRoute(userId: String, phoneNumber: String) ="register_screen/$userId/$phoneNumber"
    }

}

// sealed class restricts inheritance  . each screen is considered as object.