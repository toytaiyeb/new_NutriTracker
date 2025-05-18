package com.example.nutritrack.presentation.screens.main

import android.app.TimePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nutritrack.R
import com.example.nutritrack.database.DatabaseBuilder
import com.example.nutritrack.presentation.navigation.route.Screen
import com.example.nutritrack.presentation.theme.NutriTrackTheme
import com.example.nutritrack.presentation.theme.Purple
import com.example.nutritrack.presentation.theme.fonts.Fonts
import com.example.nutritrack.repository.FoodIntakeRepository
import com.example.nutritrack.utils.Utils
import com.example.nutritrack.factory.GlobalViewModelFactory
import com.example.nutritrack.viewmodel.QuestionnaireViewModel
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun QuestionnaireScreen(navController: NavController) {

    val context = LocalContext.current
    val userId = Utils.getUserId(context) // Retrieve user ID from SharedPreferences

    val foodCategories = listOf(
        "Fruits",
        "Vegetables",
        "Grains",
        "Red Meat",
        "Seafood",
        "Poultry",
        "Fish",
        "Eggs",
        "Nuts/Seeds"
    )
    val personas = listOf(
        PersonaData("Health Devotee", "I’m passionate about healthy eating..."),
        PersonaData("Mindful Eater", "I’m health-conscious..."),
        PersonaData("Wellness Striver", "I aspire to be healthy..."),
        PersonaData("Balance Seeker", "I try and live a balanced lifestyle..."),
        PersonaData("Health Procrastinator", "I’m contemplating healthy eating..."),
        PersonaData("Food Carefree", "I’m not bothered about healthy eating...")
    )
    var showDialog by remember { mutableStateOf(false) } // state for showing dialog for persona details

    // States for UI tracking and interactions
    var selectedDescription by remember { mutableStateOf("") }
    val selectedCategories = remember { mutableStateMapOf<String, Boolean>() }
    var selectedPersona by remember { mutableStateOf("") }
    var dropDownSelectedPersona by remember { mutableStateOf("") }
    var biggestMealTime by remember { mutableStateOf("00:00") }
    var sleepTime by remember { mutableStateOf("00:00") }
    var wakeTime by remember { mutableStateOf("00:00") }
    val dropdownExpanded = remember { mutableStateOf(false) } // state for dropdown menu for choosing persona


    val database = DatabaseBuilder.getInstance(context)
    val foodIntakeDao = database.foodIntakeDao()

    // Create the FoodIntakeRepository instance
    val foodIntakeRepository = remember { FoodIntakeRepository(foodIntakeDao) }

    // ViewModel instance with the repository
    val viewModel: QuestionnaireViewModel = viewModel(
        factory = GlobalViewModelFactory(foodIntakeRepository)
    )


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Food Intake Questionnaire") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        // Collect all questionnaire data
                        val selectedCategoriesString = selectedCategories.filter { it.value }
                            .keys.joinToString(",") // Create a comma-separated string of selected categories

                        // Save data to Room using ViewModel (this will overwrite if userId already exists)
                        viewModel.saveFoodIntakeData(
                            userId = userId!!,  // Get userId from SharedPreferences or wherever it's stored
                            selectedCategories = selectedCategoriesString,
                            selectedPersona = dropDownSelectedPersona,
                            biggestMealTime = biggestMealTime,
                            sleepTime = sleepTime,
                            wakeTime = wakeTime
                        )

                        // Navigate to BottomNavigation screen
                        navController.navigate(Screen.BottomNavigation.route)
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .width(130.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Save", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Save",
                        color = Color.White,
                        fontFamily = Fonts.Konnect,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Tick all the food categories you can eat",
                fontWeight = FontWeight.SemiBold,
                fontFamily = Fonts.Konnect
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 300.dp),
                horizontalArrangement = Arrangement.spacedBy(1.dp),
            ) {
                items(foodCategories) { item ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = selectedCategories[item] == true,
                            onCheckedChange = {
                                selectedCategories[item] = it
                            }
                        )
                        Text(
                            item,
                            fontWeight = FontWeight.Medium,
                            fontFamily = Fonts.Konnect,
                            color = Color.Black,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "Your Persona",
                fontWeight = FontWeight.SemiBold,
                fontFamily = Fonts.Konnect
            )

            Text(
                "People can be broadly classified into 6 different types based on their eating preferences...",
                fontWeight = FontWeight.Medium,
                fontFamily = Fonts.Konnect,
                fontSize = 13.sp,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                personas.forEach { personaData ->
                    Button(
                        onClick = {
                            selectedPersona = personaData.name
                            selectedDescription = personaData.description
                            showDialog = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Purple),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(8.dp),
                        elevation = ButtonDefaults.buttonElevation(2.dp)
                    ) {
                        Text(
                            text = personaData.name,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontFamily = Fonts.Konnect,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            if (showDialog) {
                PersonaDialog(
                    persona = selectedPersona,
                    description = selectedDescription,
                    imageId = when (selectedPersona) {
                        "Health Devotee" -> R.drawable.persona_1
                        "Mindful Eater" -> R.drawable.persona_2
                        "Wellness Striver" -> R.drawable.persona_3
                        "Balance Seeker" -> R.drawable.persona_4
                        "Health Procrastinator" -> R.drawable.persona_5
                        "Food Carefree" -> R.drawable.persona_6
                        else -> R.drawable.img // Fallback image
                    },
                    onDismiss = { showDialog = false }
                )
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                thickness = 1.dp,
                color = Color(0xFFCCCCCC)
            )


            Spacer(modifier = Modifier.height(16.dp)) // spacer between persona and dropdown.
            Text(
                "Which persona best fits you?",
                fontWeight = FontWeight.SemiBold,
                fontFamily = Fonts.Konnect
            )


            ExposedDropdownMenuBox(
                expanded = dropdownExpanded.value,
                onExpandedChange = {
                    dropdownExpanded.value = !dropdownExpanded.value
                } // toggle dropdown
            ) {
                OutlinedTextField(
                    value = dropDownSelectedPersona, // set selected persona
                    onValueChange = {},
                    readOnly = true,
                    shape = RoundedCornerShape(16.dp),
                    label = {
                        Text(
                            "Select options",
                            fontFamily = Fonts.Konnect,
                            fontWeight = FontWeight.Medium
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded.value)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu( // dropdown menu for personas
                    expanded = dropdownExpanded.value,
                    onDismissRequest = { dropdownExpanded.value = false }
                ) {
                    personas.forEach { persona ->
                        androidx.compose.material3.DropdownMenuItem( // dropdown menu item for each persona
                            text = { Text(persona.name) }, // set persona name
                            onClick = {
                                dropDownSelectedPersona = persona.name
                                dropdownExpanded.value = false
                            }
                        )
                    }
                }

            }



            Spacer(modifier = Modifier.height(16.dp))

            Text("Timings", fontWeight = FontWeight.SemiBold, fontFamily = Fonts.Konnect)

            TimeRow(
                "What time of day approx. do you normally eat your biggest meal?",
                biggestMealTime
            ) { biggestMealTime = it }

            TimeRow(
                "What time of day approx. do you go to sleep at night?",
                sleepTime
            ) { sleepTime = it }

            TimeRow(
                "What time of day approx. do you wake up in the morning?",
                wakeTime
            ) { wakeTime = it }

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = Color(0xFFCCCCCC)
            )
        }
    }
}


//@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
//@Composable
//fun QuestionnaireScreen(navController: NavController) {
//
//    val systemUiController = rememberSystemUiController()// system UI controller foe status bar customization
//
//    // Set status bar color and icon color
//    SideEffect {
//        systemUiController.setStatusBarColor(
//            color = Color.Black, // Purple
//            darkIcons = true // false = white icons
//        )
//    }
//    val foodCategories = listOf( // list of food categories
//        "Fruits",
//        "Vegetables",
//        "Grains",
//        "Red Meat",
//        "Seafood",
//        "Poultry",
//        "Fish",
//        "Eggs",
//        "Nuts/Seeds"
//    )
//
//    val personas = listOf( // list of personas
//        PersonaData(
//            "Health Devotee",
//            "I’m passionate about healthy eating & health plays a big part in my life. I use social media to follow active lifestyle personalities or get new recipes/exercise ideas. I may even buy superfoods or follow a particular type of diet. I like to think I am super healthy."
//        ),
//        PersonaData(
//            "Mindful Eater",
//            "I’m health-conscious and being healthy and eating healthy is important to me. Although health means different things to different people, I make conscious lifestyle decisions about eating based on what I believe healthy means. I look for new recipes and healthy eating information on social media."
//        ),
//        PersonaData(
//            "Wellness Striver",
//            "I aspire to be healthy (but struggle sometimes). Healthy eating is hard work! I’ve tried to improve my diet, but always find things that make it difficult to stick with the changes. Sometimes I notice recipe ideas or healthy eating hacks, and if it seems easy enough, I’ll give it a go."
//        ),
//        PersonaData(
//            "Balance Seeker",
//            "I try and live a balanced lifestyle, and I think that all foods are okay in moderation. I shouldn’t have to feel guilty about eating a piece of cake now and again. I get all sorts of inspiration from social media like finding out about new restaurants, fun recipes and sometimes healthy eating tips."
//        ),
//        PersonaData(
//            "Health Procrastinator",
//            "I’m contemplating healthy eating but it’s not a priority for me right now. I know the basics about what it means to be healthy, but it doesn’t seem relevant to me right now. I have taken a few steps to be healthier but I am not motivated to make it a high priority because I have too many other things going on in my life."
//        ),
//        PersonaData(
//            "Food Carefree",
//            "I’m not bothered about healthy eating. I don’t really see the point and I don’t think about it. I don’t really notice healthy eating tips or recipes and I don’t care what I eat."
//        ),
//
//        )
//
//    // states for tracking selctions and UI interactions
//    var selectedDescription by remember { mutableStateOf("") } // description of selected persona
//    val selectedCategories = remember { mutableStateMapOf<String, Boolean>() } // map of selected categories of food items
//    val dropdownExpanded = remember { mutableStateOf(false) } // state for dropdown menu for choosing persona
//    var showDialog by remember { mutableStateOf(false) } // state for showing dialog for persona details
//    var selectedPersona by remember { mutableStateOf("") } //  select persona
//    var dropDownSelectedPersona by remember { mutableStateOf("") } // remember selected persona from dropdown
//
//    remember { mutableStateOf(false) }
//
//    var biggestMealTime by remember { mutableStateOf("00:00") } // remember biggest meal time
//    var sleepTime by remember { mutableStateOf("00:00") } // remember sleep time
//    var wakeTime by remember { mutableStateOf("00:00") } // remember wake up time
//
//    val context = LocalContext.current
//    Scaffold(
//        topBar = {  // displays the top bar with title and back button
//            TopAppBar(
//                title = {
//                    Text(
//                        "Food Intake Questionnaire", // title of the screen
//                        fontFamily = Fonts.Konnect, // font family
//                        fontWeight = FontWeight.Medium, // font weight
//                        color = Color.Black // text color
//                    )
//                },
//                navigationIcon = {
//                    // Navigation icon to navigate back
//                    IconButton(onClick = { navController.navigateUp() }) { // navigate back to previous screen
//                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") // back icon
//                    }
//                }
//            )
//        },
//
//
//        bottomBar = { // bottom box bar with save button
//            Box(
//                modifier = Modifier.fillMaxWidth(),
//                contentAlignment = Alignment.Center
//            ) {
//                Button(
//                    onClick = {
//                        saveDataToSharedPreferences(
//                            context = context,// save data to shared preferences
//                            selectedCategories = selectedCategories,// save data to shared preferences
//                            dropDownSelectedPersona = dropDownSelectedPersona,// save data to shared preferences
//                            biggestMealTime = biggestMealTime,// save data to shared preferences
//                            sleepTime = sleepTime, // save data to shared preferences
//                            wakeTime = wakeTime, // save data to shared preferences
//                        )
//                        navController.navigate(Screen.BottomNavigation.route) }, // navigate to bottom navigation screen
//                    modifier = Modifier // modifier for button
//                        .padding(16.dp)
//                        .width(130.dp),
//                    shape = RoundedCornerShape(10.dp),
//                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
//                ) {
//                    Icon(Icons.Default.Check, contentDescription = "Save", tint = Color.White)
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text(
//                        "Save", // text for button
//                        color = Color.White, // text color
//                        fontFamily = Fonts.Konnect, // font family
//                        fontWeight = FontWeight.Medium // font weight
//                    )
//                }
//            }
//        }
//    ) { paddingValues ->
//        Column( // main column for the screen
//            modifier = Modifier
//                .padding(paddingValues)
//                .padding(16.dp)
//                .verticalScroll(rememberScrollState()) // enable scrolling
//
//        ) {
//
//
//            Spacer(modifier = Modifier.height(16.dp)) // spacer for spacing above text.
//
//            Text(
//                "Tick all the food categories you can eat",
//                fontWeight = FontWeight.SemiBold,
//                fontFamily = Fonts.Konnect
//            )
//
//            LazyVerticalGrid( // grid view for food categories
//                columns = GridCells.Fixed(3), // number of columns
//                modifier = Modifier
//                    .fillMaxWidth() // fill the width
//                    .heightIn(max = 300.dp),// set max height
//                horizontalArrangement = Arrangement.spacedBy(1.dp),
//
//            ) {
//                items(foodCategories) { item ->
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,// vertical alignment
//                    ) {
//                        Checkbox(
//                            checked = selectedCategories[item] == true,
//                            onCheckedChange = {
//                                selectedCategories[item] = it
//                            }
//                        )
//                        Text(
//                            item,
//                            fontWeight = FontWeight.Medium,
//                            fontFamily = Fonts.Konnect,
//                            color = Color.Black,
//                            fontSize = 12.sp
//                        )
//                    }
//                }
//            }
//
//
//            Spacer(modifier = Modifier.height(12.dp)) // spacer after food categories
//
//            Text(
//                "Your Persona", fontWeight = FontWeight.SemiBold,
//                fontFamily = Fonts.Konnect
//            )
//
//            Text(
//                "People can be broadly classified into 6 different types based on their eating preferences. Click on each button below to find out the different types, and select the type that best fits you!",
//                fontWeight = FontWeight.Medium,
//                fontFamily = Fonts.Konnect,
//                fontSize = 13.sp,
//                modifier = Modifier.padding(vertical = 4.dp),
//                lineHeight = 16.sp
//            )
//
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            FlowRow( // flow row for personas
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(8.dp),
//                verticalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                personas.forEach { personaData ->
//                    Button(
//                        onClick = {
//                            selectedPersona = personaData.name // set selected persona
//                            selectedDescription = personaData.description // set selected description
//                            showDialog = true
//                        },
//                        colors = ButtonDefaults.buttonColors(containerColor = Purple),// set button colors
//                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp), // set button padding
//                        shape = RoundedCornerShape(8.dp), // set button shape
//                        elevation = ButtonDefaults.buttonElevation(2.dp) // set button elevation
//                    ) {
//                        Text(
//                            text = personaData.name,
//                            color = Color.White,
//                            fontSize = 11.sp,
//                            fontFamily = Fonts.Konnect,
//                            fontWeight = FontWeight.Medium
//                        )
//                    }
//                }
//            }
//
//            if (showDialog) {
//                PersonaDialog(
//                    persona = selectedPersona,
//                    description = selectedDescription,
//                    imageId = when (selectedPersona) {
//                        "Health Devotee" -> R.drawable.persona_1
//                        "Mindful Eater" -> R.drawable.persona_2
//                        "Wellness Striver" -> R.drawable.persona_3
//                        "Balance Seeker" -> R.drawable.persona_4
//                        "Health Procrastinator" -> R.drawable.persona_5
//                        "Food Carefree" -> R.drawable.persona_6
//                        else -> R.drawable.img // Fallback image
//                    },
//                    onDismiss = { showDialog = false }
//                )
//            }
//            Spacer(modifier = Modifier.height(16.dp))
//
//
//            HorizontalDivider(
//                modifier = Modifier
//                    .fillMaxWidth(),
//                thickness = 1.dp,
//                color = Color(0xFFCCCCCC)
//            )
//
//
//            Spacer(modifier = Modifier.height(16.dp)) // spacer between persona and dropdown.
//            Text(
//                "Which persona best fits you?",
//                fontWeight = FontWeight.SemiBold,
//                fontFamily = Fonts.Konnect
//            )
//
//
//            ExposedDropdownMenuBox(
//                expanded = dropdownExpanded.value,
//                onExpandedChange = { dropdownExpanded.value = !dropdownExpanded.value } // toggle dropdown
//            ) {
//                OutlinedTextField(
//                    value = dropDownSelectedPersona, // set selected persona
//                    onValueChange = {},
//                    readOnly = true,
//                    shape = RoundedCornerShape(16.dp),
//                    label = {
//                        Text(
//                            "Select options",
//                            fontFamily = Fonts.Konnect,
//                            fontWeight = FontWeight.Medium
//                        )
//                    },
//                    trailingIcon = {
//                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded.value)
//                    },
//                    modifier = Modifier
//                        .menuAnchor()
//                        .fillMaxWidth()
//                )
//
//                ExposedDropdownMenu( // dropdown menu for personas
//                    expanded = dropdownExpanded.value,
//                    onDismissRequest = { dropdownExpanded.value = false }
//                ) {
//                    personas.forEach { persona ->
//                        androidx.compose.material3.DropdownMenuItem( // dropdown menu item for each persona
//                            text = { Text(persona.name) }, // set persona name
//                            onClick = {
//                                dropDownSelectedPersona = persona.name
//                                dropdownExpanded.value = false
//                            }
//                        )
//                    }
//                }
//
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))// spacer after dropdown.
//
//            Text("Timings", fontWeight = FontWeight.SemiBold, fontFamily = Fonts.Konnect)
//
//            TimeRow(
//                "What time of day approx. do you normally eat your biggest meal?",
//                biggestMealTime)
//            {
//                biggestMealTime = it
//            }
//
//            TimeRow("What time of day approx. do you go to sleep at night?",
//                sleepTime)
//            {
//                sleepTime = it
//            }
//
//            TimeRow("What time of day approx. do you wake up in the morning?",
//                wakeTime)
//            {
//                wakeTime = it
//            }
//
//            HorizontalDivider(
//                modifier = Modifier
//                    .fillMaxWidth(),
//                thickness = 1.dp,
//                color = Color(0xFFCCCCCC)
//            )
//
//        }
//    }
//}
//
//
@Composable
fun TimeRow(label: String, value: String, onValueChange: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    val timePickerDialog = remember {
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                val formattedTime = String.format("%02d:%02d", hourOfDay, minute)
                onValueChange(formattedTime)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        )
    }

    Row(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = label,
            fontWeight = FontWeight.Medium,
            fontFamily = Fonts.Konnect,
            fontSize = 12.sp,
            color = Color.Black,
            lineHeight = 16.sp,
            modifier = Modifier
                .weight(0.5f)
                .padding(end = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = {},
            modifier = Modifier
                .clickable { timePickerDialog.show() }
                .weight(0.3f),
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            readOnly = true,
            leadingIcon = {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = "Check",
                    modifier = Modifier.clickable {
                        timePickerDialog.show()
                    })
            }
        )
    }
}

@Composable
fun PersonaDialog(persona: String, description: String, imageId: Int, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White, shape = RoundedCornerShape(8.dp))
        ) {
            Image(
                painter = painterResource(id = imageId),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = persona,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = Fonts.Konnect,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = Fonts.Konnect,
                lineHeight = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier
                    .padding(bottom = 8.dp),
                onClick = { onDismiss() },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Purple)
            ) {
                Text(
                    text = "Dismiss",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Fonts.Konnect
                )
            }
        }
    }
}

//
//fun saveDataToSharedPreferences(
//    context: Context,
//    selectedCategories: Map<String, Boolean>,
//    dropDownSelectedPersona: String,
//    biggestMealTime: String,
//    sleepTime: String,
//    wakeTime: String
//) {
//    val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
//    val userId = sharedPref.getString("USER_ID", null) ?: return
//
//    val selectedCategoriesString = selectedCategories.filter { it.value }
//        .keys.joinToString(",")
//
//    with(sharedPref.edit()) {
//        putString("$userId.selectedCategories", selectedCategoriesString)
//        putString("$userId.dropDownSelectedPersona", dropDownSelectedPersona)
//        putString("$userId.biggestMealTime", biggestMealTime)
//        putString("$userId.sleepTime", sleepTime)
//        putString("$userId.wakeTime", wakeTime)
//        apply()
//    }
//
//    Log.d("PREF_DEBUG", "Saved for $userId → Categories: $selectedCategoriesString, Persona: $dropDownSelectedPersona")
//}
//
//
//
//
//
data class PersonaData(val name: String, val description: String)

@Preview(showBackground = true)
@Composable
fun QuestionnaireScreenPreview() {
    NutriTrackTheme {
        val navController = rememberNavController()
        QuestionnaireScreen(navController = navController)
    }
}

