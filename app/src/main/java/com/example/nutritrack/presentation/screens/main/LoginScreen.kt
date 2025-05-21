package com.example.nutritrack.presentation.screens.main

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nutritrack.R
import com.example.nutritrack.database.DatabaseBuilder
import com.example.nutritrack.model.Patient
import com.example.nutritrack.presentation.navigation.route.Screen
import com.example.nutritrack.presentation.theme.NutriTrackTheme
import com.example.nutritrack.presentation.theme.Purple
import com.example.nutritrack.presentation.theme.fonts.Fonts
import com.example.nutritrack.repository.PatientRepository
import com.example.nutritrack.utils.Utils
import com.example.nutritrack.utils.isFirstLaunch
import com.example.nutritrack.utils.markFirstLaunchComplete
import com.example.nutritrack.viewmodel.LoginViewModel
import com.example.nutritrack.factory.LoginViewModelFactory
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val db = DatabaseBuilder.getInstance(context)
    val repository = PatientRepository(db.patientDao())
    val viewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(repository))

    val systemUi = rememberSystemUiController()

    var selectedUserId by remember { mutableStateOf("") }
    var enteredPhone by remember { mutableStateOf("") }
    var enteredPassword by remember { mutableStateOf("") }
    var errorVisible by remember { mutableStateOf(false) }
    var dropdownExpanded by remember { mutableStateOf(false) }
    var isUserClaimed by remember { mutableStateOf(false) }

    val patientList by viewModel.patients.collectAsState()

    LaunchedEffect(Unit) {
        if (isFirstLaunch(context)) {
            val loadedPatients = loadPatientsFromCSV(context)
            repository.insertPatients(loadedPatients)
            markFirstLaunchComplete(context)
        }
        viewModel.loadPatients()
    }

    val availableIds = patientList.map { it.userId }

    SideEffect {
        systemUi.setStatusBarColor(Color.White, darkIcons = false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Purple)
        )

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = (-20).dp),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = Color.White,
            tonalElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Welcome Back",
                    fontFamily = Fonts.Konnect,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Dropdown for selecting UserID
                ExposedDropdownMenuBox(
                    expanded = dropdownExpanded,
                    onExpandedChange = { dropdownExpanded = !dropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedUserId,
                        onValueChange = {},
                        readOnly = true,
                        label = {
                            Text("User ID", fontFamily = Fonts.Konnect, fontSize = 14.sp)
                        },
                        placeholder = {
                            Text("Choose your ID", color = Color.Gray)
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                    )

                    ExposedDropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false }
                    ) {
                        availableIds.forEach { id ->
                            DropdownMenuItem(
                                text = { Text(id, fontFamily = Fonts.Konnect) },
                                onClick = {
                                    selectedUserId = id
                                    dropdownExpanded = false
                                    viewModel.getPatient(id) { patient ->
                                        isUserClaimed = patient?.name?.isNotBlank() == true &&
                                                patient.password?.isNotBlank() == true
                                    }
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (isUserClaimed) {
                    OutlinedTextField(
                        value = enteredPassword,
                        onValueChange = { enteredPassword = it },
                        label = { Text("Password", fontFamily = Fonts.Konnect) },
                        placeholder = { Text("Enter password") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = PasswordVisualTransformation()
                    )
                } else {
                    OutlinedTextField(
                        value = enteredPhone,
                        onValueChange = { enteredPhone = it },
                        label = { Text("Phone Number", fontFamily = Fonts.Konnect) },
                        placeholder = { Text("Enter phone number") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Login to proceed or claim your account using your phone number.",
                    fontSize = 12.sp,
                    fontFamily = Fonts.Konnect,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (errorVisible) {
                    Text(
                        text = "Invalid credentials. Please try again.",
                        color = Color.Red,
                        fontSize = 14.sp,
                        fontFamily = Fonts.Konnect
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.getPatient(selectedUserId) { patient ->
                            if (patient != null) {
                                errorVisible = false
                                if (isUserClaimed) {
                                    if (enteredPassword.isBlank()) {
                                        errorVisible = true
                                    } else {
                                        viewModel.validateUserByPassword(
                                            selectedUserId,
                                            enteredPassword
                                        ) { valid ->
                                            if (valid) {
                                                Utils.saveUserId(context, selectedUserId)
                                                navController.navigate(Screen.Questionnaire.route)
                                            } else {
                                                errorVisible = true
                                            }
                                        }
                                    }
                                } else {
                                    viewModel.validateUserByNumber(
                                        selectedUserId,
                                        enteredPhone
                                    ) { valid ->
                                        if (valid) {
                                            navController.navigate(
                                                Screen.Register.createRoute(
                                                    selectedUserId,
                                                    patient.phoneNumber ?: ""
                                                )
                                            )
                                        } else {
                                            errorVisible = true
                                        }
                                    }
                                }
                            } else {
                                errorVisible = true
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Purple)
                ) {
                    Text(
                        text = if (isUserClaimed) "Log In" else "Continue",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontFamily = Fonts.Konnect,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}


suspend fun loadPatientsFromCSV(context: Context): List<Patient> {
    val patients = mutableListOf<Patient>()

    withContext(Dispatchers.IO) {
        val inputStream = context.assets.open("data.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))
        val headerRow = reader.readLine()
        val headers = headerRow.split(",")

        val columnMap = headers.withIndex().associate { it.value to it.index }

        reader.forEachLine { line ->
            val values = line.split(",")

            patients.add(
                Patient(
                    userId = values[columnMap["User_ID"] ?: -1],
                    phoneNumber = values[columnMap["PhoneNumber"] ?: -1],
                    sex = values[columnMap["Sex"] ?: -1],
                    heifaTotalScore = values[columnMap["HEIFAtotalscoreFemale"]
                        ?: -1].toFloatOrNull() ?: 0f,
                    vegetablesScore = values[columnMap["VegetablesHEIFAscoreFemale"]
                        ?: -1].toFloatOrNull() ?: 0f,
                    fruitScore = values[columnMap["FruitHEIFAscoreFemale"] ?: -1].toFloatOrNull()
                        ?: 0f,
                    grainsCerealScore = values[columnMap["GrainsandcerealsHEIFAscoreFemale"]
                        ?: -1].toFloatOrNull() ?: 0f,
                    wholeGrainsScore = values[columnMap["WholegrainsHEIFAscoreFemale"]
                        ?: -1].toFloatOrNull() ?: 0f,
                    meatAlternativesScore = values[columnMap["MeatandalternativesHEIFAscoreFemale"]
                        ?: -1].toFloatOrNull() ?: 0f,
                    dairyAlternativesScore = values[columnMap["DairyandalternativesHEIFAscoreFemale"]
                        ?: -1].toFloatOrNull() ?: 0f,
                    waterScore = values[columnMap["WaterHEIFAscoreFemale"] ?: -1].toFloatOrNull()
                        ?: 0f,
                    unsaturatedFatScore = values[columnMap["UnsaturatedFatHEIFAscoreFemale"]
                        ?: -1].toFloatOrNull() ?: 0f,
                    sodiumScore = values[columnMap["SodiumHEIFAscoreFemale"] ?: -1].toFloatOrNull()
                        ?: 0f,
                    sugarScore = values[columnMap["SugarHEIFAscoreFemale"] ?: -1].toFloatOrNull()
                        ?: 0f,
                    alcoholScore = values[columnMap["AlcoholHEIFAscoreFemale"]
                        ?: -1].toFloatOrNull() ?: 0f,
                    discretionaryScore = values[columnMap["DiscretionaryHEIFAscoreFemale"]
                        ?: -1].toFloatOrNull() ?: 0f
                )
            )
        }
        reader.close()
    }
    return patients
}


@Preview(showBackground = true)// it makes backgrounf visible
@Composable
fun LoginScreenPreview() {
    NutriTrackTheme {
        val navController =
            rememberNavController()// creates a dummy navcontroller. so it doesn't break in preview
        LoginScreen(navController = navController) // shows actual login screen.
    }
}