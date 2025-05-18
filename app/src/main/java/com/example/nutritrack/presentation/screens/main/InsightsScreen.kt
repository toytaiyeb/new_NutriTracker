package com.example.nutritrack.presentation.screens.main

import android.content.Intent
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.nutritrack.database.DatabaseBuilder
import com.example.nutritrack.presentation.navigation.route.Screen
import com.example.nutritrack.presentation.theme.NutriTrackTheme
import com.example.nutritrack.presentation.theme.Purple
import com.example.nutritrack.presentation.theme.fonts.Fonts
import com.example.nutritrack.repository.PatientRepository
import com.example.nutritrack.utils.Utils.getUserId
import com.example.nutritrack.viewmodel.LoginViewModel
import com.example.nutritrack.factory.LoginViewModelFactory

@Composable
fun TickSlider(
    value: Float, // slider value
    max: Int, // max value
    modifier: Modifier = Modifier,
    tickCount: Int = 5, // number of ticks
    color: Color = Purple // color of the slider
) {
    // Box for the slider
    Box(modifier = modifier.height(32.dp)) {
        // Tick layer
        Canvas(
            modifier = Modifier
                .matchParentSize()
                .padding(horizontal = 20.dp)
        ) {
            val width = size.width
            val centerY = size.height / 2
            val radius = 2.5.dp.toPx()
            val spacing = width / (tickCount - 1)

            val filledWidth = (value / max.toFloat()).coerceIn(0f, 1f) * width

            for (i in 0 until tickCount) {
                val x = i * spacing
                val tickColor = if (x <= filledWidth) color else color.copy(alpha = 0.2f)
                drawCircle(tickColor, radius = radius, center = Offset(x, centerY))
            }
        }

        // Slider track + white thumb
        // newValue -> // sliderValues[index] = newValue if WE need to change the value

        Slider(
            value = value,
            onValueChange = {},
            valueRange = 0f..max.toFloat(),
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            colors = SliderDefaults.colors(
                disabledThumbColor = Color.White,
                disabledActiveTrackColor = color,
                disabledInactiveTrackColor = color.copy(alpha = 0.2f)
            )
        )

        // Outline thumb ring
        Canvas(
            modifier = Modifier
                .matchParentSize()
                .padding(horizontal = 12.dp)
        ) {
            val width = size.width
            val centerY = size.height / 2
            val thumbX = (value / max.toFloat()).coerceIn(0f, 1f) * width
            drawCircle(
                color = color,
                radius = 7.5.dp.toPx(),
                center = Offset(thumbX, centerY),
                style = Stroke(width = 1.5.dp.toPx())
            )
        }
    }
}

@Composable
fun InsightsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val primaryPurple = Purple

    val currentUserId = getUserId(context)?:""
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

    val sliderValues = remember { mutableStateListOf<Float>() }

    LaunchedEffect(categories) {
        sliderValues.clear()
        sliderValues.addAll(categories.map { (_, value, max) ->
            value.toFloat().coerceAtMost(max.toFloat())
        })
    }

    Column( // main column for the screen
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        // title text

        Text(
            text = "Insights: Food Score",
            fontSize = 20.sp,
            fontFamily = Fonts.Konnect,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // slider for each category
        if (sliderValues.size == categories.size) {
            categories.forEachIndexed { index, (label, _, max) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Text(
                        text = label,
                        modifier = Modifier.weight(0.35f),
                        fontSize = 14.sp,
                        fontFamily = Fonts.Konnect,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                    )

                    TickSlider(
                        value = sliderValues[index],
                        max = max,
                        tickCount = if (max <= 5) max else 5,
                        modifier = Modifier.weight(0.45f),
                        color = primaryPurple
                    )

                    Text(
                        text = "${sliderValues[index]}/$max", // slider value
                        modifier = Modifier
                            .weight(0.2f)
                            .padding(start = 8.dp),
                        textAlign = TextAlign.End,
                        fontSize = 14.sp,
                        fontFamily = Fonts.Konnect,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                    )
                }
            }
            // spacer between sliders and total food quality score
            Spacer(modifier = Modifier.height(24.dp))

            val totalCurrent = sliderValues.sum()
            val totalMax = 100
            Text(
                text = "Total Food Quality Score",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = Fonts.Konnect,
                color = Color.Black,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                TickSlider(
                    value = totalCurrent.toFloat(),
                    max = totalMax.toInt(),
                    tickCount = 5,
                    modifier = Modifier
                        .weight(0.8f)
                        .clip(RoundedCornerShape(50)),
                    color = primaryPurple
                )

                Text(
                    text = "$total/100", // total from csv and out from 100
                    modifier = Modifier
                        .weight(0.2f)
                        .padding(start = 8.dp),
                    textAlign = TextAlign.End,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Fonts.Konnect,
                    color = Color.Black
                )
            }
        } else {
            Spacer(modifier = Modifier.height(24.dp))
            CircularProgressIndicator(
                color = primaryPurple,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
            // share button for sharing the score with others
        Button(
            onClick = {
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "See I'm getting healthy week by week")
                    type = "text/plain"
                }
                context.startActivity(Intent.createChooser(shareIntent, "Share via"))
            },
            colors = ButtonDefaults.buttonColors(containerColor = primaryPurple),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(Icons.Default.Share, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Share with someone",
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontFamily = Fonts.Konnect,
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick =
            {
                navController.navigate(Screen.NutriCoach.route) {
                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = primaryPurple),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 100.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(Icons.Default.Face, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Improve my diet!",
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontFamily = Fonts.Konnect,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InsightsScreenPreview() {
    NutriTrackTheme {
        val navController = rememberNavController()
        InsightsScreen(navController = navController)
    }
}
