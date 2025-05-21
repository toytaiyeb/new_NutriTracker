package com.example.nutritrack.presentation.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.nutritrack.presentation.navigation.route.Screen
import com.example.nutritrack.presentation.theme.NutriTrackTheme
import com.example.nutritrack.presentation.theme.Purple
import com.example.nutritrack.presentation.theme.fonts.Fonts
import com.example.nutritrack.repository.PatientRepository
import com.example.nutritrack.utils.isFirstLaunch
import com.example.nutritrack.utils.markFirstLaunchComplete
import com.example.nutritrack.viewmodel.LoginViewModel
import com.example.nutritrack.factory.LoginViewModelFactory
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    // system UI controller foe status bar customization
    val systemUiController = rememberSystemUiController()
    val selectedId = navController.currentBackStackEntry?.arguments?.getString("userId") ?: ""
    val phoneNumber = navController.currentBackStackEntry?.arguments?.getString("phoneNumber") ?: ""

    val context = LocalContext.current
    val database = DatabaseBuilder.getInstance(context)
    val repository = PatientRepository(database.patientDao())
    val viewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(repository))

//    var selectedId by remember { mutableStateOf("") }
//    var phoneNumber by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    val patients by viewModel.patients.collectAsState()

    LaunchedEffect(Unit) {
        if (isFirstLaunch(context)) {
            val patientsFromCsv = loadPatientsFromCSV(context)
            repository.insertPatients(patientsFromCsv)
            markFirstLaunchComplete(context)
        }
        viewModel.loadPatients()
    }

    val idOptions = patients.map { it.userId }
    // side effect to set status bar color to white.
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.White, // Purple
            darkIcons = false // false = white icons
        )
    }
    // main container layout.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
    ) {
        // Top Purple background Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Purple),
        )

        // Main Rounded white container
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = (-20).dp), // shifts white surface on box
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = Color.White,
            tonalElevation = 4.dp
        ) {
            // content column
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                // login title
                Text(
                    text = "Register",
                    fontFamily = Fonts.Konnect,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(24.dp))

                //  userID selection dropdown box
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = selectedId,
                        onValueChange = {},
                        label = {
                            Text(
                                stringResource(R.string.my_id_provided_by_your_clinician),
                                fontWeight = FontWeight.Medium,
                                fontFamily = Fonts.Konnect,
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                        },
                        placeholder = {
                            Text(
                                text = "Select your id",
                                fontFamily = Fonts.Konnect,
                                fontWeight = FontWeight.Normal,
                                color = Color.Gray
                            )
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                    )

//
                }


                Spacer(modifier = Modifier.height(16.dp))

                // Phone number input
                OutlinedTextField(
                    value = phoneNumber,
                    readOnly = true,
                    onValueChange = { },
                    label = {
                        Text(
                            stringResource(R.string.phone_number),
                            fontWeight = FontWeight.Medium,
                            fontFamily = Fonts.Konnect,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    },
                    placeholder = {
                        Text(
                            stringResource(R.string.enter_your_number),
                            fontFamily = Fonts.Konnect,
                            fontWeight = FontWeight.Normal,
                            color = Color.Gray
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Phone
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Phone number input
                OutlinedTextField(
                    value = userName,
                    onValueChange = { userName = it },
                    label = {
                        Text(
                            text = stringResource(R.string.name),
                            fontWeight = FontWeight.Medium,
                            fontFamily = Fonts.Konnect,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.enter_your_name),
                            fontFamily = Fonts.Konnect,
                            fontWeight = FontWeight.Normal,
                            color = Color.Gray
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Phone
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Phone number input
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = {
                        Text(
                            stringResource(R.string.password),
                            fontWeight = FontWeight.Medium,
                            fontFamily = Fonts.Konnect,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.enter_your_password),
                            fontFamily = Fonts.Konnect,
                            fontWeight = FontWeight.Normal,
                            color = Color.Gray
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password
                    ),
                    visualTransformation = PasswordVisualTransformation()

                )


                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = {
                        Text(
                            text = stringResource(R.string.confirm_password),
                            fontWeight = FontWeight.Medium,
                            fontFamily = Fonts.Konnect,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.enter_your_password_again),
                            fontFamily = Fonts.Konnect,
                            fontWeight = FontWeight.Normal,
                            color = Color.Gray
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password
                    ),
                    visualTransformation = PasswordVisualTransformation()
                )


                Spacer(modifier = Modifier.height(24.dp))

                // Info text before continue button
                Text(
                    text = stringResource(R.string.login_text),
                    fontSize = 12.sp,
                    color = Color.Black,
                    fontFamily = Fonts.Konnect,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                if (showError) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(vertical = 8.dp),
                        fontFamily = Fonts.Konnect
                    )
                }

                // Continue Button which leads to Questionnaire
                Button(
                    onClick = { // Inside your Continue button onClick:

                        if (userName.isBlank()) {
                            showError = true
                            errorMessage = "Name cannot be empty"
                        } else if (password.isBlank()) {
                            showError = true
                            errorMessage = "Password cannot be empty"
                        } else if (confirmPassword.isBlank()) {

                            showError = true
                            errorMessage = "Confirm password cannot be empty"
                        } else if (password != confirmPassword) {
                            showError = true
                            errorMessage = "Passwords do not match"

                        } else {
                            showError = false
                            // Save name and password in the database
                            viewModel.claimAccount(selectedId, userName, password) {

                                navController.navigate(Screen.Login.route)
                            }
                            // Optionally, navigate to a different screen (e.g., home or login)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Purple)
                ) {
                    Text(
                        "Register",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        fontFamily = Fonts.Konnect
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Continue Button which leads to Questionnaire
                Button(
                    onClick = {
                        navController.navigateUp()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Purple)
                ) {
                    Text(
                        "Login",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        fontFamily = Fonts.Konnect
                    )
                }
            }
        }


    }


}


@Preview(showBackground = true)// it makes backgrounf visible
@Composable
fun RegisterScreenPreview() {
    NutriTrackTheme {
        val navController =
            rememberNavController()// creates a dummy navcontroller. so it doesn't break in preview
        RegisterScreen(navController = navController) // shows actual login screen.
    }
}