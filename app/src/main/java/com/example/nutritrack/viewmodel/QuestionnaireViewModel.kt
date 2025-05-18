package com.example.nutritrack.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutritrack.repository.FoodIntakeRepository
import kotlinx.coroutines.launch

class QuestionnaireViewModel(private val foodIntakeRepository: FoodIntakeRepository) : ViewModel() {

    // Function to save food intake data
    fun saveFoodIntakeData(
        userId: String,
        selectedCategories: String,
        selectedPersona: String,
        biggestMealTime: String,
        sleepTime: String,
        wakeTime: String
    ) {
        viewModelScope.launch {
            try {
                foodIntakeRepository.saveFoodIntakeData(
                    userId,
                    selectedCategories,
                    selectedPersona,
                    biggestMealTime,
                    sleepTime,
                    wakeTime
                )
            } catch (e: Exception) {
                // Handle error if needed
                Log.e("QuestionnaireViewModel", "Error saving food intake data: ${e.message}")
            }
        }
    }
}
