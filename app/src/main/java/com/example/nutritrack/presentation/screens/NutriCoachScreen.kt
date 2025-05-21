package com.example.nutritrack.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
    val context = LocalContext.current
    val apiKey = stringResource(R.string.api_key_gemini)

    var searchQuery by remember { mutableStateOf("") }
    val fruitViewModel: FruitViewModel = viewModel(
        factory = FruitViewModelFactory(FruitRepository(RetrofitClient.apiService))
    )
    val database = DatabaseBuilder.getInstance(context)
    val nutriCoachTipsViewModel: NutriCoachTipsViewModel = viewModel(
        factory = NutriCoachTipsViewModelFactory(
            NutriCoachTipsRepository(
                GeminiRetrofitClient.apiService,
                database.nutriCoachTipsDao()
            )
        )
    )

    val fruitDetailsResult by fruitViewModel.fruitDetails.observeAsState()
    val motivationalMessageResult by nutriCoachTipsViewModel.motivationalMessage.observeAsState()
    val savedMessages by nutriCoachTipsViewModel.savedMessages.observeAsState(emptyList())

    var fruitDetails by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    val defaultDetails = mapOf(
        "Family" to "N/A", "Calories" to "N/A", "Fat" to "N/A",
        "Sugar" to "N/A", "Carbohydrates" to "N/A", "Protein" to "N/A"
    )

    var showDialog by remember { mutableStateOf(false) }
    var motivationalMessage by remember { mutableStateOf("No message available") }

    val detailsToShow = if (fruitDetails.isEmpty()) defaultDetails else fruitDetails

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            item {
                Text(
                    text = "NutriCoach Assistant",
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = Fonts.Konnect,
                    fontSize = 22.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            item {
                Column {
                    Text(
                        text = "Search Fruit",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        fontFamily = Fonts.Konnect
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = {
                                Text("e.g. Apple", fontFamily = Fonts.Konnect)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            shape = RoundedCornerShape(25.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color(0xFFF1F1F1),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Button(
                            onClick = {
                                if (searchQuery.isNotBlank()) {
                                    fruitViewModel.fetchFruitDetails(searchQuery)
                                }
                            },
                            shape = RoundedCornerShape(25.dp),
                            colors = ButtonDefaults.buttonColors(Purple),
                            modifier = Modifier.height(56.dp)
                        ) {
                            Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
                        }
                    }
                }
            }

            item {
                fruitDetailsResult?.let { result ->
                    when (result) {
                        is com.example.nutritrack.utils.Result.Success -> {
                            result.data?.let { fruit ->
                                fruitDetails = mapOf(
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
                                text = "Could not fetch data.",
                                color = Color.Red,
                                fontFamily = Fonts.Konnect
                            )
                        }

                        else -> Unit
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        detailsToShow.forEach { (key, value) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(key, fontFamily = Fonts.Konnect, fontWeight = FontWeight.SemiBold)
                                Text(value, fontFamily = Fonts.Konnect)
                            }
                        }
                    }
                }
            }

            item {
                Button(
                    onClick = { nutriCoachTipsViewModel.fetchMotivationalMessage(apiKey) },
                    colors = ButtonDefaults.buttonColors(containerColor = Purple),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.chat),
                        contentDescription = "Chat Icon",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Get AI Motivation",
                        fontFamily = Fonts.Konnect,
                        color = Color.White
                    )
                }
            }

            item {
                motivationalMessageResult?.let {
                    when (it) {
                        is com.example.nutritrack.utils.Result.Success -> {
                            motivationalMessage = it.data ?: "Stay positive!"
                        }

                        is com.example.nutritrack.utils.Result.Error -> {
                            motivationalMessage = "Error fetching message"
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF6FF))
                ) {
                    Text(
                        text = motivationalMessage,
                        fontFamily = Fonts.Konnect,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            item {
                Button(
                    onClick = {
                        nutriCoachTipsViewModel.fetchSavedMessages()
                        showDialog = true
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Purple),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 80.dp)
                ) {
                    Text(
                        "Show All Tips",
                        color = Color.White,
                        fontFamily = Fonts.Konnect
                    )
                }
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color.White
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "All AI Tips",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    textAlign = TextAlign.Center
                )

                LazyColumn(modifier = Modifier.height(400.dp)) {
                    items(savedMessages) { tip ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA))
                        ) {
                            Text(
                                text = tip.message,
                                modifier = Modifier.padding(12.dp),
                                fontFamily = Fonts.Konnect
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Purple)
                ) {
                    Text("Close", color = Color.White)
                }
            }
        }
    }
}
