package com.example.nutritrack.utils

import android.content.Context

object Utils {


    fun getUserId(context: Context): String? {
        val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        return sharedPref.getString("USER_ID", null)
    }

    fun saveUserId(context: Context, userId: String) {
        val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("USER_ID", userId)
            apply()
        }
    }

    fun logout(context: Context) {
        val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear()
            apply()
        }
    }

}
// for declaring just user_id in the code JUST ONE TIME , otherwise needed to do it alot of time.
// singleton utility class for getting user id. get's userID from shared preferences.without repeating code.