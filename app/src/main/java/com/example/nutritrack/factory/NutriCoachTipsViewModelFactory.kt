package com.example.nutritrack.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nutritrack.repository.NutriCoachTipsRepository
import com.example.nutritrack.viewmodel.NutriCoachTipsViewModel

class NutriCoachTipsViewModelFactory(private val repository: NutriCoachTipsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NutriCoachTipsViewModel::class.java)) {
            return NutriCoachTipsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
