package com.example.nutritrack.presentation.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nutritrack.R
import com.example.nutritrack.database.DatabaseBuilder
import com.example.nutritrack.presentation.navigation.route.Screen
import com.example.nutritrack.presentation.theme.fonts.Fonts
import com.example.nutritrack.repository.PatientRepository
import com.example.nutritrack.utils.Utils
import com.example.nutritrack.utils.Utils.getUserId
import com.example.nutritrack.viewmodel.LoginViewModel
import com.example.nutritrack.factory.LoginViewModelFactory

@Composable
fun SettingsScreen(navController: NavController) {

    val context = LocalContext.current
    val currentUserId = getUserId(context) ?:""
    val database = DatabaseBuilder.getInstance(context)
    val repository = PatientRepository(database.patientDao())
    val viewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(repository))
    var id by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    val showDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {

        viewModel.getPatient(currentUserId) {
            id = it!!.userId
            name = it.name
            phone = it.phoneNumber

        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Account Section
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Settings",
            fontWeight = FontWeight.Bold,
            fontFamily = Fonts.Konnect,
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Account".uppercase(),
            fontSize = 14.sp,
            color = Color.Gray,
            fontFamily = Fonts.Konnect,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(16.dp))


        // Account Info (Name, Phone, ID)
        Column() {
            SettingItem(
                icon = R.drawable.user,
                label = name
            )
            Spacer(modifier = Modifier.height(8.dp))

            SettingItem(
                icon = R.drawable.phone,
                label = phone
            )
            Spacer(modifier = Modifier.height(8.dp))

            SettingItem(
                icon = R.drawable.patient_id,
                label = id
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(
            color = Color.LightGray,
            thickness = 1.dp
        )

        Spacer(modifier = Modifier.height(24.dp))


        // Other Settings Section
        Text(
            text = "Other Settings".uppercase(),
            fontSize = 14.sp,
            color = Color.Gray,
            fontFamily = Fonts.Konnect,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(16.dp))


        // Other Settings Options (Logout, Clinician Login)
        Column() {
            SettingItem(
                icon = R.drawable.logout,
                label = "Logout",
                isArrow = true,
                onClick = { showDialog.value = true  }
            )
            Spacer(modifier = Modifier.height(8.dp))
            SettingItem(
                icon = R.drawable.user,
                label = "Clinician Login",
                isArrow = true,
                onClick = {navController.navigate(Screen.Clinician.route) }
            )
        }

        if (showDialog.value) {
            LogoutConfirmationDialog(
                onDismiss = { showDialog.value = false },
                onConfirm = {
                    // Handle logout action here
                    showDialog.value = false
                    // Navigate to login or handle the logout process

                    Utils.logout(context)
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }

                }
            )
        }
    }
}

@Composable
fun SettingItem(
    icon: Int,
    label: String,
    isArrow: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick?.invoke() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(Color.Gray)

        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            fontFamily = Fonts.Konnect,
            modifier = Modifier.weight(1f)
        )
        if (isArrow) {
            Image(
                painter = painterResource(R.drawable.img_1),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                colorFilter = ColorFilter.tint(Color.Gray)
            )
        }
    }
}

@Composable
fun LogoutConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Are you sure you want to logout?", fontFamily = Fonts.Konnect, fontWeight = FontWeight.SemiBold) },
        text = { Text("You will be logged out of your account.", fontFamily = Fonts.Konnect, fontWeight = FontWeight.Medium) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Logout", fontFamily = Fonts.Konnect, fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", fontFamily = Fonts.Konnect, fontWeight = FontWeight.SemiBold)
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(navController = rememberNavController())
}
