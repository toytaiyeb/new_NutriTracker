package com.example.nutritrack.presentation.theme.fonts

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.nutritrack.R

object Fonts {
    val Konnect = FontFamily(
        Font(R.font.konnectlight, FontWeight.Light),
        Font(R.font.konnectregular, FontWeight.Normal),
        Font(R.font.konnect_medium, FontWeight.Medium),
        Font(R.font.konnect_semi_bold, FontWeight.SemiBold),
        Font(R.font.konnectbold, FontWeight.Bold)
    )
}