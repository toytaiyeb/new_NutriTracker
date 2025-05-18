package com.example.nutritrack.repository

import com.example.nutritrack.dao.FoodIntakeDao
import com.example.nutritrack.model.FoodIntake

class FoodIntakeRepository(private val foodIntakeDao: FoodIntakeDao) {

    // Insert food intake data for the user
    suspend fun saveFoodIntakeData(
        userId: String,
        selectedCategories: String,
        selectedPersona: String,
        biggestMealTime: String,
        sleepTime: String,
        wakeTime: String
    ) {
        val foodIntake = FoodIntake(
            userId = userId, // Link to Patient (foreign key)
            selectedCategories = selectedCategories,
            selectedPersona = selectedPersona,
            biggestMealTime = biggestMealTime,
            sleepTime = sleepTime,
            wakeTime = wakeTime
        )
        // Insert the food intake data into the database
        foodIntakeDao.insertFoodIntake(foodIntake)
    }

    // Optional: Fetch food intake data for the user (if needed)
    suspend fun getFoodIntakeByUserId(userId: String): List<FoodIntake> {
        return foodIntakeDao.getFoodIntakeByUserId(userId)
    }
}
