package com.example.nutritrack.presentation.screens.main


import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
    var averageHEIFAMale by remember { mutableStateOf(0.0) }
    var averageHEIFAFemale by remember { mutableStateOf(0.0) }

    val insights = listOf(
        "Variable Water Intake: Consumption of water varies greatly among the users in this dataset, with scores ranging from 0 to 100. There isn't a clear, immediate correlation in this small sample between water intake score and the overall HEIFA score, though some high scorers did have high water intake.",
        "Low Wholegrain Consumption: The intake of wholegrains appears generally low across this group. Only one user in the provided sample data had a recorded intake and score for wholegrains, while the rest had zero.",
        "Potential Gender Difference in HEIFA Scoring: The data includes columns for both HEIFAtotalscoreMale and HEIFAtotalscoreFemale for each user. In several cases, the potential score calculated for females is slightly higher than that calculated for males, suggesting the HEIFA criteria might result in slightly different potential maximums or scoring based on gender recommendations, independent of the actual user's intake."
    )

    val context = LocalContext.current
    val database = DatabaseBuilder.getInstance(context)
    val repository = PatientRepository(database.patientDao())
    val viewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(repository))

    LaunchedEffect(Unit) {

        viewModel.getAllPatient { patients ->
            Log.e("patients", patients.toString())
            if (patients != null) {
                averageHEIFAMale = String.format("%.2f", patients.filter { it.sex == "Male" }
                    .map { it.heifaTotalScore }
                    .average()
                ).toDouble()

                averageHEIFAFemale = String.format("%.2f", patients.filter { it.sex == "Female" }
                    .map { it.heifaTotalScore }
                    .average()
                ).toDouble()
            }
        }

    }

    // Layout for Clinician Dashboard
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        // Title
        Text(
            text = "Clinician Dashboard",
            fontWeight = FontWeight.SemiBold,
            fontFamily = Fonts.Konnect,
            fontSize = 24.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        // HEIFA Scores Row
        Column(modifier = Modifier.fillMaxWidth()) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
                    .background(Color.LightGray, RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Average HEIFA (Male)",
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = Fonts.Konnect,
                        fontSize = 14.sp
                    )
                    Text(
                        text = ":",
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = Fonts.Konnect,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp),
                        textAlign = TextAlign.End
                    )
                }


                Text(
                    text = "$averageHEIFAMale",
                    fontWeight = FontWeight.Normal,
                    fontFamily = Fonts.Konnect,
                    modifier = Modifier
                        .weight(0.4f)
                        .fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
                    .background(Color.LightGray, RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Average HEIFA (Female)",
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = Fonts.Konnect,
                        fontSize = 14.sp
                    )
                    Text(
                        text = ":",
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = Fonts.Konnect,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp),
                        textAlign = TextAlign.End
                    )
                }


                Text(
                    text = "$averageHEIFAFemale",
                    fontWeight = FontWeight.Normal,
                    fontFamily = Fonts.Konnect,
                    modifier = Modifier
                        .weight(0.4f)
                        .fillMaxWidth()
                )
            }


        }

        Spacer(modifier = Modifier.height(16.dp))

        // "Find Data Pattern" button
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = Purple),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(50.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Details Icon",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Find Data Pattern",
                        color = Color.White,
                        fontFamily = Fonts.Konnect,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Insights Cards
        insights.forEach { insight ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .background(Color.White, RoundedCornerShape(8.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Text(
                    text = insight,
                    fontFamily = Fonts.Konnect,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        // Done button at the bottom
        Spacer(modifier = Modifier.weight(1f)) // Push Done button to the bottom
        Button(
            onClick = { /* Handle "Done" action */ },
            colors = ButtonDefaults.buttonColors(containerColor = Purple),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Done",
                color = Color.White,
                fontFamily = Fonts.Konnect,
                fontWeight = FontWeight.Medium
            )
        }
    }
}


@Preview(showBackground = true)// it makes backgrounf visible
@Composable
fun ClinicianDashboardScreenPreview() {
    NutriTrackTheme {
        val navController =
            rememberNavController()// creates a dummy navcontroller. so it doesn't break in preview
        ClinicianDashboardScreen(navController = navController) // shows actual login screen.
    }
}