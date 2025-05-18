package com.example.nutritrack.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nutritrack.repository.FoodIntakeRepository
import com.example.nutritrack.viewmodel.QuestionnaireViewModel

class GlobalViewModelFactory(
    private val foodIntakeRepository: FoodIntakeRepository,
    // Add other repositories or dependencies as needed
) : ViewModelProvider.Factory {

    // We use a map to store all repository dependencies
    private val repositories: Map<Class<*>, Any> = mapOf(
        QuestionnaireViewModel::class.java to foodIntakeRepository
        // Add other repositories here if needed
    )

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = repositories[modelClass]
        if (repository != null) {
            return when (modelClass) {
                QuestionnaireViewModel::class.java -> QuestionnaireViewModel(repository as FoodIntakeRepository) as T
                // Handle other ViewModels if needed here
                else -> throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
