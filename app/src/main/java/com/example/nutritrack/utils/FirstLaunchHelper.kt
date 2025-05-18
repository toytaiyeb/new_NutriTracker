package com.example.nutritrack.utils

import android.content.Context

fun isFirstLaunch(context: Context): Boolean {
    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    return prefs.getBoolean("first_launch", true)
}

fun markFirstLaunchComplete(context: Context) {
    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    prefs.edit().putBoolean("first_launch", false).apply()
}
