package com.example.nutritrack.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.nutritrack.R
import com.example.nutritrack.client.GeminiRetrofitClient
import com.example.nutritrack.client.RetrofitClient
import com.example.nutritrack.database.DatabaseBuilder
import com.example.nutritrack.factory.NutriCoachTipsViewModelFactory
import com.example.nutritrack.factory.FruitViewModelFactory
import com.example.nutritrack.model.NutriCoachTips
import com.example.nutritrack.presentation.theme.NutriTrackTheme
import com.example.nutritrack.presentation.theme.Purple
import com.example.nutritrack.presentation.theme.fonts.Fonts
import com.example.nutritrack.repository.NutriCoachTipsRepository
import com.example.nutritrack.repository.FruitRepository
import com.example.nutritrack.viewmodel.NutriCoachTipsViewModel
import com.example.nutritrack.viewmodel.FruitViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutriCoachScreen(navController: NavHostController) {
    // Remember the search text (fruit name)

    var searchQuery by remember { mutableStateOf("") }
    val fruitViewModel: FruitViewModel = viewModel(
        factory = FruitViewModelFactory(FruitRepository(RetrofitClient.apiService))
    )
    val context = LocalContext.current
    val database = DatabaseBuilder.getInstance(context)
    val nutriCoachTipsViewModel: NutriCoachTipsViewModel = viewModel(
        factory = NutriCoachTipsViewModelFactory(
            NutriCoachTipsRepository(
                apiService = GeminiRetrofitClient.apiService,
                dao = database.nutriCoachTipsDao()
            )
        )
    )

    // Observe LiveData from ViewModel
    val motivationalMessageResult by nutriCoachTipsViewModel.motivationalMessage.observeAsState()
    val savedMessages by nutriCoachTipsViewModel.savedMessages.observeAsState(emptyList())


    // Observe the fruit details
    val fruitDetailsResult by fruitViewModel.fruitDetails.observeAsState()

    val fruitDetails = remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    val defaultDetails = mapOf(
        "Family" to "N/A",
        "Calories" to "N/A",
        "Fat" to "N/A",
        "Sugar" to "N/A",
        "Carbohydrates" to "N/A",
        "Protein" to "N/A"
    )
    val apiKey = stringResource(R.string.api_key_gemini)

    val detailsToShow = if (fruitDetails.value.isEmpty()) defaultDetails else fruitDetails.value
    // State to control showing the dialog
    var showDialog by remember { mutableStateOf(false) }

    // Motivational message from AI
    var motivationalMessage by remember { mutableStateOf("No message available") }

    Box(modifier = Modifier.fillMaxSize()) { // Use Box to position the button at the bottom
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Title
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "NutriCoach",
                fontWeight = FontWeight.Bold,
                fontFamily = Fonts.Konnect,
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Fruit Name",
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Fonts.Konnect,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically // Center items vertically
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(end = 8.dp)
                            .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
                            .background(Color.White, RoundedCornerShape(16.dp))
                    ) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = {
                                Text(
                                    "Banana",
                                    fontFamily = Fonts.Konnect,
                                    fontWeight = FontWeight.Normal
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )
                    }

                    Button(
                        onClick = {
                            if (searchQuery.isNotBlank()) {
                                fruitViewModel.fetchFruitDetails(searchQuery)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Purple),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.height(45.dp),
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Details Icon",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Details",
                                color = Color.White,
                                fontFamily = Fonts.Konnect,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (val result = fruitDetailsResult) {
                is com.example.nutritrack.utils.Result.Success -> {
                    result.data?.let { fruit ->

                        fruitDetails.value = mapOf(
                            "Family" to fruit.family,
                            "Calories" to fruit.nutritions.calories.toString(),
                            "Fat" to fruit.nutritions.fat.toString(),
                            "Sugar" to fruit.nutritions.sugar.toString(),
                            "Carbohydrates" to fruit.nutritions.carbohydrates.toString(),
                            "Protein" to fruit.nutritions.protein.toString()
                        )


                    }
                }

                is com.example.nutritrack.utils.Result.Error -> {
                    Text(
                        text = "No Data Found",
                        fontFamily = Fonts.Konnect,
                        fontWeight = FontWeight.Medium,
                        color = Color.Red
                    )
                }

                else -> {

                }
            }
            // Fruit Details
            Column(modifier = Modifier.fillMaxWidth()) {
                detailsToShow.forEach { (label, value) ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
                            .background(Color.LightGray, RoundedCornerShape(16.dp))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Row(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "$label",
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = Fonts.Konnect,
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
                            text = value,
                            fontWeight = FontWeight.Normal,
                            fontFamily = Fonts.Konnect,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(16.dp))


            Button(
                onClick = { nutriCoachTipsViewModel.fetchMotivationalMessage(apiKey) },
                colors = ButtonDefaults.buttonColors(containerColor = Purple),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.chat),
                        contentDescription = "Details Icon",
                        colorFilter = ColorFilter.tint(Color.White),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Motivational Message (AI)",
                        color = Color.White,
                        fontFamily = Fonts.Konnect,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))
            motivationalMessageResult?.let {
                when (it) {
                    is com.example.nutritrack.utils.Result.Success -> {
                        motivationalMessage = it.data ?: "No message available"
                    }

                    is com.example.nutritrack.utils.Result.Error -> {
                        Text(text = "Error: ${it.exception.message}")
                    }
                }
            }
            Text(
                text = motivationalMessage,
                fontFamily = Fonts.Konnect,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 17.sp,
                color = Color.Black,
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 16.dp)
            )
        }

        // Always position the "Show All Tips" button at the bottom
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomEnd)
                .padding(bottom = 90.dp, end = 16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Button(
                onClick = {
                    nutriCoachTipsViewModel.fetchSavedMessages()
                    showDialog = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = Purple),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            ) {
                Text(
                    text = "Show All Tips",
                    color = Color.White,
                    fontFamily = Fonts.Konnect,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        if (showDialog) {
            ShowAllTipsDialog(savedMessages = savedMessages) {
                showDialog = false
            }
        }
    }
}


@Composable
fun ShowAllTipsDialog(savedMessages: List<NutriCoachTips>, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "AI Tips",
                    fontFamily = Fonts.Konnect,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 16.dp),
                    textAlign = TextAlign.Center
                )

                // Display each saved AI tip
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                ) {
                    items(savedMessages) { message ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 2.dp, vertical = 8.dp),
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)

                        ) {
                            Text(
                                text = message.message,
                                fontFamily = Fonts.Konnect,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Done Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Button(
                        onClick = { onDismiss() },
                        colors = ButtonDefaults.buttonColors(containerColor = Purple),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
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
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NutriCoachScreenPreview() {
    NutriTrackTheme {
        NutriCoachScreen(rememberNavController())
    }
}
