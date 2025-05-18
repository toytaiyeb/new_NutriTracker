package com.example.nutritrack.presentation.navigation.bottomnav


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.nutritrack.presentation.theme.NutriTrackTheme
import com.example.nutritrack.presentation.theme.Purple
import com.example.nutritrack.presentation.theme.fonts.Fonts.Konnect


@Composable
fun CustomBottomNavigation(navController: NavHostController, currentRoute: String) {
// list of all bottom navigation items
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Insights,
        BottomNavItem.NutriCoach,
        BottomNavItem.Settings,
    )

    //  Explicitly declare the type as BottomNavItem to avoid type mismatch error
    var selectedItem: BottomNavItem by remember { mutableStateOf(BottomNavItem.Home) }
    // Update selectedItem based on the current route
    LaunchedEffect(currentRoute) {
        selectedItem = items.firstOrNull { it.route == currentRoute } ?: BottomNavItem.Home
    }
    //main box container for bottom navigation bar
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
            // Row for the items icon and label
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable() {
                                // Update selectedItem and navigate to the corresponding screen
                            selectedItem = item //
                            navController.navigate(item.route) {
                                // prevent multiple copies of the same destination
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }

                        }
                ) {
                    // Icon and label for NAV each item
                    Image(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                        modifier = Modifier.size(26.dp),
                        colorFilter = if (selectedItem == item) ColorFilter.tint(Purple) else null
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.title,
                        color = if (selectedItem == item) Purple else Color.Gray,
                        fontFamily = Konnect,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun CustomBottomNavigationPreview() {
    NutriTrackTheme   {
        val navController = rememberNavController()
        CustomBottomNavigation(navController = navController, currentRoute = "home")
    }
}