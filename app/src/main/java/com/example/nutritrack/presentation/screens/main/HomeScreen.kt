package com.example.nutritrack.presentation.screens.main

// importing all required  androidX and compose imports
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
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


    // for  status bar  color.
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Black, // Black.
            darkIcons = true // false = white icons
        )
    }
    // Main container of screen
    Column(
        modifier = Modifier
            .fillMaxSize() // entire screen
            .background(Color.White)// set the background white
            .padding(16.dp)// set the padding 16 to the screen on all sides.
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        // Greeting
        Text(
            text = "Hello,",
            fontFamily = Fonts.Konnect,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = Color.Gray
        )

        Text(
            text = "$name , $gender",// retrives the user name from shared preferences and gender.
            fontFamily = Fonts.Konnect,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            fontSize = 24.sp
        )
        // again spacer
        Spacer(modifier = Modifier.height(8.dp))

        // Description and Edit Button Row
        Row(
            verticalAlignment = Alignment.CenterVertically,// set the alignment to center
            horizontalArrangement = Arrangement.SpaceBetween,// set the arrangement to space between
            modifier = Modifier.fillMaxWidth()// set the width to fill the screen
        ) {
            Text(
                text = "You've already filled in your Food Intake\nQuestionnaire, but you can change details here:",// given text
                color = Color.Black,
                fontFamily = Fonts.Konnect,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f),
                fontSize = 12.sp,
                lineHeight = 16.sp,

                )
            // edit button to navigate to questionnaire screen
            Button(
                onClick = {
                    navController.navigate(Screen.Questionnaire.route)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Purple),// set the button color to purple
                shape = RoundedCornerShape(8.dp),// set the shape to rounded
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 8.dp
                ),// set the padding to 16 to all sides
                modifier = Modifier.padding(start = 8.dp)// set the padding to 8 to the start
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Edit",// set the text to edit
                    color = Color.White,// set the color to white
                    fontFamily = Fonts.Konnect,// set the font family to konnect
                    fontWeight = FontWeight.Medium,// set the font weight to medium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Image
        Image(
            painter = painterResource(id = R.drawable.img), // Normal image
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Score Header Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "My Score",// set the text to my score
                fontFamily = Fonts.Konnect,// set the font family to konnect
                fontWeight = FontWeight.Medium,// set the font weight to medium
                color = Color.Black // set the color to black
            )

            Text(
                text = "See all scores >",// set the text to see all scores
                fontFamily = Fonts.Konnect,// set the font family to konnect
                fontWeight = FontWeight.Medium,// set the font weight to medium
                color = Color.Gray,// set the color to gray
                fontSize = 12.sp// set the font size to 12
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Score Display
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                Icons.Default.KeyboardArrowUp,
                contentDescription = null,
                tint = Color.Gray
            ) // arrow icon
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Your Food Quality score", // set the text to your food quality score
                fontFamily = Fonts.Konnect, // set the font family to konnect
                fontWeight = FontWeight.Medium, // set the font weight to medium
            )
            Spacer(modifier = Modifier.weight(1f)) // spacer to push the score to the right
            val totalCurrent =
                categories.sumOf { it.second.toDouble() }.toFloat() // calculate the total score
            val totalMax = categories.sumOf { it.third } // calculate the maximum score
            Text(
                text = "$total/100",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF43A047), // set the color to green
                    fontFamily = Fonts.Konnect,
                    fontWeight = FontWeight.Medium,
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(),
            thickness = 1.dp,
            color = Color(0xFFCCCCCC)
        )// horizontal divider

        Spacer(modifier = Modifier.height(24.dp))

        // Description
        Text(
            text = "What is the Food Quality Score?",
            fontFamily = Fonts.Konnect,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Your Food Quality Score provides a snapshot of how well your eating patterns align with established food guidelines, helping you identify both strengths and opportunities for improvement in your diet.",
            fontFamily = Fonts.Konnect,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray,
            fontSize = 12.sp,
            lineHeight = 17.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "This personalized measurement considers various food groups including vegetables, fruits, whole grains, and proteins to give you practical insights for making healthier food choices.",
            fontFamily = Fonts.Konnect,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray,
            fontSize = 12.sp,
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