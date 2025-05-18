package com.example.nutritrack.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutritrack.model.FruitDetails
import com.example.nutritrack.repository.FruitRepository
import kotlinx.coroutines.launch

class FruitViewModel(private val repository: FruitRepository) : ViewModel() {

    private val _fruitDetails = MutableLiveData<com.example.nutritrack.utils.Result<FruitDetails>>()
    val fruitDetails: LiveData<com.example.nutritrack.utils.Result<FruitDetails>> get() = _fruitDetails

    fun fetchFruitDetails(fruitName: String) {
        viewModelScope.launch {
            _fruitDetails.value = repository.getFruitDetails(fruitName)
        }
    }
}
