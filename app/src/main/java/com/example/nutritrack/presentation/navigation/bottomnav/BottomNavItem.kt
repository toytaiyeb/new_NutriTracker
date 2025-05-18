package com.example.nutritrack.presentation.navigation.bottomnav

import com.example.nutritrack.R
import com.example.nutritrack.presentation.navigation.route.Screen

//sealed class represents each item in the bottom navigation bar
sealed class BottomNavItem(
    val route: String,// Navigation route for each item
    val icon: Int,// icon for each item
    val title: String // label shown under the icon
) {
    // home screen tab in bottom navigation
    object Home :
        BottomNavItem(
            Screen.Home.route,// home screen route
            R.drawable.home,// home screen icon
            "Home" // home screen tab
        )

        // insights screen tab
    object Insights :
        BottomNavItem(
            Screen.Insights.route,// insights screen route
            R.drawable.insights,// insights screen icon
            "Insights")// insights screen tab

    //Nutricoach screen tab
    object NutriCoach :
        BottomNavItem(
            Screen.NutriCoach.route,
            R.drawable.brain,
            "NutriCoach"
        )
        // settings screen tab
    object Settings : BottomNavItem(
        Screen.Settings.route,
        R.drawable.setting,
        "Settings"
    )

    object Clinician : BottomNavItem(
        Screen.Clinician.route,
        R.drawable.setting,
        "Settings"
    )
    object ClinicianDashboard : BottomNavItem(
        Screen.ClinicianDashboard.route,
        R.drawable.setting,
        "Settings"
    )

}