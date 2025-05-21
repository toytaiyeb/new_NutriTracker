package com.example.nutritrack.presentation.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nutritrack.R
import com.example.nutritrack.presentation.navigation.route.Screen
import com.example.nutritrack.presentation.theme.NutriTrackTheme
import com.example.nutritrack.presentation.theme.Purple
import com.example.nutritrack.presentation.theme.fonts.Fonts
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClinicianLoginScreen(navController: NavController) {
    // system UI controller foe status bar customization
    val systemUiController = rememberSystemUiController()

    val context = LocalContext.current


    var selectedId by remember { mutableStateOf("") }
    var clinicianKey by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }


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
            .background(Color.White)
    ) {


        // Main Rounded white container
        Surface(
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
                Spacer(modifier = Modifier.height(32.dp))
                // login title
                Text(
                    text = "Clinician Login",
                    fontFamily = Fonts.Konnect,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = Color.Black
                )



                Spacer(modifier = Modifier.height(16.dp))


                    OutlinedTextField(
                        value = clinicianKey,
                        onValueChange = { clinicianKey = it },
                        label = {
                            Text(
                                "Clinician Key",
                                fontWeight = FontWeight.Medium,
                                fontFamily = Fonts.Konnect,
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                        },
                        placeholder = {
                            Text(
                               "Enter your clinician key",
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






                Spacer(modifier = Modifier.height(32.dp))

                // error messsage display

                if (showError) {
                    Text(
                        text = "Invalid clinician key",
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(vertical = 8.dp),
                        fontFamily = Fonts.Konnect
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Continue Button which leads to Questionnaire
                Button(
                    onClick = { // Inside your Continue button onClick:

                        if (clinicianKey == "d"){

                            navController.navigate(Screen.ClinicianDashboard.route)
                        }else{
                            showError = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Purple)
                ) {
                    Image(
                        painter = androidx.compose.ui.res.painterResource(id = R.drawable.logout),
                        contentDescription = null,
                        modifier = Modifier
                            .size(26.dp)
                            .padding(4.dp),
                        colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White)
                    )
                    Spacer(modifier = Modifier.padding(start = 8.dp))
                    Text(
                       "Clinician Login",
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
fun ClinicianLoginScreenPreview() {
    NutriTrackTheme {
        val navController =
            rememberNavController()// creates a dummy navcontroller. so it doesn't break in preview
        ClinicianLoginScreen(navController = navController) // shows actual login screen.
    }
}