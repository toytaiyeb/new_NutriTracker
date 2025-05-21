package com.example.nutritrack.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nutritrack.repository.NutriCoachTipsRepository
import com.example.nutritrack.viewmodel.NutriCoachTipsViewModel

/**
 * Factory for creating an instance of NutriCoachTipsViewModel with a repository dependency.
 */
class NutriCoachTipsViewModelFactory(
    private val tipRepo: NutriCoachTipsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Ensure the requested ViewModel is of the expected type
        if (modelClass.isAssignableFrom(NutriCoachTipsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NutriCoachTipsViewModel(tipRepo) as T
        }
        throw IllegalArgumentException("Invalid ViewModel class: ${modelClass.name}")
    }
}
