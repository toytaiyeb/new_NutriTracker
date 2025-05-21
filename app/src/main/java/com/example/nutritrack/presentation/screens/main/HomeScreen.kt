package com.example.nutritrack.presentation.screens.main

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.nutritrack.R
import com.example.nutritrack.database.DatabaseBuilder
import com.example.nutritrack.presentation.navigation.route.Screen
import com.example.nutritrack.presentation.theme.NutriTrackTheme
import com.example.nutritrack.presentation.theme.Purple
import com.example.nutritrack.presentation.theme.fonts.Fonts
import com.example.nutritrack.repository.PatientRepository
import com.example.nutritrack.utils.Utils
import com.example.nutritrack.viewmodel.LoginViewModel
import com.example.nutritrack.factory.LoginViewModelFactory
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun HomeScreen(navController: NavHostController) {
    val systemUiController = rememberSystemUiController()
    val context = LocalContext.current

    val currentUserId = Utils.getUserId(context) ?: ""
    val database = DatabaseBuilder.getInstance(context)
    val repository = PatientRepository(database.patientDao())
    val viewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(repository))

    var gender by remember { mutableStateOf("") }
    var total by remember { mutableStateOf(0f) }
    var categories by remember { mutableStateOf(listOf<Triple<String, Float, Int>>()) }
    var name by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getPatient(currentUserId) {
            gender = it!!.sex
            total = it.heifaTotalScore
            name = it.name
            Log.e("curretn", it.toString())
            categories = listOf(
                Triple("Vegetables", it.vegetablesScore, 10),
                Triple("Fruits", it.fruitScore, 10),
                Triple("Grains & Cereals", it.grainsCerealScore, 10),
                Triple("Whole Grains", it.wholeGrainsScore, 10),
                Triple("Meat & Alternatives", it.meatAlternativesScore, 10),
                Triple("Dairy", it.dairyAlternativesScore, 10),
                Triple("Water", it.waterScore, 5),
                Triple("Unsaturated Fats", it.unsaturatedFatScore, 10),
                Triple("Sodium", it.sodiumScore, 10),
                Triple("Sugar", it.sugarScore, 10),
                Triple("Alcohol", it.alcoholScore, 5),
                Triple("Discretionary Foods", it.discretionaryScore, 10),
            )
        }
    }

    SideEffect {
        systemUiController.setStatusBarColor(Color(0xFF121212), darkIcons = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Hi Again,",
            fontFamily = Fonts.Konnect,
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp,
            color = Color.Gray
        )

        Text(
            text = "$name ($gender)",
            fontFamily = Fonts.Konnect,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "You have submitted your Food Intake Questionnaire.",
                    fontSize = 12.sp,
                    fontFamily = Fonts.Konnect,
                    color = Color.DarkGray,
                    lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Need to update it?",
                    fontSize = 12.sp,
                    fontFamily = Fonts.Konnect,
                    color = Color.Black
                )
            }
            Button(
                onClick = { navController.navigate(Screen.Questionnaire.route) },
                colors = ButtonDefaults.buttonColors(containerColor = Purple),
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.padding(start = 6.dp)
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Edit", color = Color.White, fontFamily = Fonts.Konnect, fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Image(
            painter = painterResource(id = R.drawable.img),
            contentDescription = "Food Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .clip(RoundedCornerShape(14.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF8F8F8), RoundedCornerShape(10.dp))
                .padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = null, tint = Color(0xFF666666))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Your Food Quality Score",
                    fontFamily = Fonts.Konnect,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "$total/100",
                    fontFamily = Fonts.Konnect,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32),
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = Color.LightGray, thickness = 0.7.dp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "About the Score",
            fontFamily = Fonts.Konnect,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Your score reflects how closely your diet aligns with recommended food guidelines. It highlights strengths and areas for improvement in your daily eating habits.",
            fontFamily = Fonts.Konnect,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 17.sp,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "We consider your intake across several food groups, including fruits, vegetables, grains, and proteins, to compute a total score out of 100.",
            fontFamily = Fonts.Konnect,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            color = Color.DarkGray,
            lineHeight = 17.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    NutriTrackTheme {
        val navController = rememberNavController()
        HomeScreen(navController = navController)
    }
}
