package com.example.nutritrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutritrack.repository.GenAIInsightsRepository
import com.example.nutritrack.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GenAIInsightsViewModel(
    private val repository: GenAIInsightsRepository
) : ViewModel() {

    private val _insights = MutableStateFlow<List<String>>(emptyList())
    val insights: StateFlow<List<String>> = _insights

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchInsights(apiKey: String) {
        viewModelScope.launch {
            when (val result = repository.fetchInsightsFromPatientData(apiKey)) {
                is Result.Success -> _insights.value = result.data ?: listOf("No insights found")
                is Result.Error -> _error.value = ""
            }
        }
    }
}
