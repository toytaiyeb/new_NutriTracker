package com.example.nutritrack.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nutritrack.repository.GenAIInsightsRepository
import com.example.nutritrack.viewmodel.GenAIInsightsViewModel

class GenAIInsightsViewModelFactory(
    private val repository: GenAIInsightsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GenAIInsightsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GenAIInsightsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
