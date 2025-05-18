package com.example.nutritrack.presentation.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.nutritrack.R
import com.example.nutritrack.presentation.navigation.route.Screen
import com.example.nutritrack.presentation.theme.NutriTrackTheme
import com.example.nutritrack.presentation.theme.Purple
import com.example.nutritrack.presentation.theme.fonts.Fonts
import com.example.nutritrack.utils.Utils


@Composable
fun WelcomeScreen(navController: NavHostController)// this is the welcome screen entry point composable function which we passed through navcontroller
{
    NutriTrackTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {

            val context = LocalContext.current
            Column(horizontalAlignment = Alignment.CenterHorizontally)// nested column for upper part of screen
            {
                // large heading screen
                Text(
                    text = "Nutritrack",// logo
                    fontSize = 32.sp,// size
                    color = Color.Black,//color
                    fontFamily = Fonts.Konnect,// konnect is font family which we used to get simillar result.
                    fontWeight = FontWeight.SemiBold,
                )

                Spacer(modifier = Modifier.height(16.dp)) // spacer

                //My nutri track with purple and light gray font  using annotated string.
                StyledNutriTrackTitle()

                //
                Spacer(modifier = Modifier.height(24.dp))
                // text below that describes the app
                Text(
                    text = "This app provides general health and nutrition information for" +
                            "educational purposes only. It is not intended as medical advice," +
                            "diagnosis, or treatment. Always consult a qualified healthcare professional before making any changes to your diet, exercise, or health regimen." +
                            "Use this app at your own risk." +
                            "If you’d like to an Accredited Practicing Dietitian (APD), please visit the Monash Nutrition/Dietetics Clinic (discounted rates for students" +
                            "): https://www.monash.edu/medicine/scs/nutrition/clinics/nutrition",
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontStyle = FontStyle.Italic,
                    lineHeight = 20.sp,
                    fontFamily = Fonts.Konnect,// konnect family  font.
                    fontWeight = FontWeight.Medium,
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // button which navigates to login screen
                Button(
                    onClick = {
                        if (Utils.getUserId(context) != null) {
                            navController.navigate(Screen.BottomNavigation.route)
                        } else {
                            navController.navigate(Screen.Login.route)// route to login screen
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Purple),
                    shape = RoundedCornerShape(8.dp)

                ) {
                    Text(
                        text = stringResource(R.string.login),
                        fontSize = 16.sp,
                        color = Color.White,
                        fontFamily = Fonts.Konnect,
                        fontWeight = FontWeight.Medium,
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Designed with ❤️ by Taiyeb Radiowala (34377190)",
                    fontSize = 12.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontFamily = Fonts.Konnect,
                    fontWeight = FontWeight.Medium,

                    )
            }
        }
    }

}


@Composable
fun StyledNutriTrackTitle() {
    // colored my nutri track
    val purple = Purple
    val gray = Color.LightGray

    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = purple, fontWeight = FontWeight.Bold)) {
                append("M")
            }
            withStyle(style = SpanStyle(color = gray, fontWeight = FontWeight.Bold)) {
                append("y")
            }
            withStyle(style = SpanStyle(color = purple, fontWeight = FontWeight.Bold)) {
                append("N")
            }
            withStyle(style = SpanStyle(color = gray, fontWeight = FontWeight.Bold)) {
                append("utri")
            }
            withStyle(style = SpanStyle(color = purple, fontWeight = FontWeight.Bold)) {
                append("T")
            }
            withStyle(style = SpanStyle(color = gray, fontWeight = FontWeight.Bold)) {
                append("rack")
            }
        },
        fontSize = 26.sp,
        modifier = Modifier.padding(16.dp),
        fontWeight = FontWeight.Bold,
        fontFamily = Fonts.Konnect,
    )
}


@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    NutriTrackTheme {
        val navController = rememberNavController() // remember the navcontroller
        WelcomeScreen(navController = navController) // passing the navcontroller to the welcome screen
    }
}
