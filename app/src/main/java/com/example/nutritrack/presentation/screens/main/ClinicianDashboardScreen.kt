package com.example.nutritrack.presentation.screens.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.nutritrack.database.DatabaseBuilder
import com.example.nutritrack.factory.LoginViewModelFactory
import com.example.nutritrack.presentation.theme.NutriTrackTheme
import com.example.nutritrack.presentation.theme.Purple
import com.example.nutritrack.presentation.theme.fonts.Fonts
import com.example.nutritrack.repository.PatientRepository
import com.example.nutritrack.viewmodel.LoginViewModel

@SuppressLint("DefaultLocale")
@Composable
fun ClinicianDashboardScreen(navController: NavHostController) {

    // Track average HEIFA scores by gender
    var heifaMaleAvg by remember { mutableStateOf(0.0) }
    var heifaFemaleAvg by remember { mutableStateOf(0.0) }

    // Sample insights to display in cards
    val insightsList = listOf(
        "Water Intake Scores vary drastically among users with no clear link to total HEIFA score.",
        "Wholegrain consumption is noticeably low, with most users scoring 0.",
        "HEIFA score potential varies by gender—females tend to have slightly higher potential totals."
    )

    // Initialize required objects for accessing DB and ViewModel
    val context = LocalContext.current
    val db = DatabaseBuilder.getInstance(context)
    val repo = PatientRepository(db.patientDao())
    val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(repo))

    // Retrieve patient data and compute averages once on screen load
    LaunchedEffect(Unit) {
        loginViewModel.getAllPatient { patientList ->
            if (patientList != null) {
                heifaMaleAvg = String.format("%.2f", patientList.filter { it.sex == "Male" }
                    .map { it.heifaTotalScore }.average()).toDouble()

                heifaFemaleAvg = String.format("%.2f", patientList.filter { it.sex == "Female" }
                    .map { it.heifaTotalScore }.average()).toDouble()
            }
        }
    }

    // Main layout container
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Dashboard title
        Text(
            text = "Clinician Dashboard",
            fontFamily = Fonts.Konnect,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        // HEIFA Scores Section
        listOf(
            "Average HEIFA (Male)" to heifaMaleAvg,
            "Average HEIFA (Female)" to heifaFemaleAvg
        ).forEach { (label, value) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                    .background(Color(0xFFF4F4F4), RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    fontFamily = Fonts.Konnect,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = ": $value",
                    fontFamily = Fonts.Konnect,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Button for finding patterns (future feature)
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = { /* Placeholder for future data pattern logic */ },
                colors = ButtonDefaults.buttonColors(containerColor = Purple),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .height(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Find Data Pattern",
                    color = Color.White,
                    fontFamily = Fonts.Konnect,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Display insight cards
        insightsList.forEach { insight ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(10.dp)),
                elevation = CardDefaults.cardElevation(2.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Text(
                    text = insight,
                    fontFamily = Fonts.Konnect,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(14.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Done button for exit or action
        Button(
            onClick = { /* Add navigation or functionality here */ },
            colors = ButtonDefaults.buttonColors(containerColor = Purple),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(
                text = "Done",
                color = Color.White,
                fontFamily = Fonts.Konnect,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun ClinicianDashboardPreview() {
    NutriTrackTheme {
        ClinicianDashboardScreen(navController = rememberNavController())
    }
}
