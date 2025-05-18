package com.example.nutritrack.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutritrack.model.NutriCoachTips
import com.example.nutritrack.repository.NutriCoachTipsRepository
import kotlinx.coroutines.launch

class NutriCoachTipsViewModel(private val repository: NutriCoachTipsRepository) : ViewModel() {

    private val _motivationalMessage = MutableLiveData<com.example.nutritrack.utils.Result<String>>()
    val motivationalMessage: LiveData<com.example.nutritrack.utils.Result<String>> get() = _motivationalMessage

    private val _savedMessages = MutableLiveData<List<NutriCoachTips>>()
    val savedMessages: LiveData<List<NutriCoachTips>> get() = _savedMessages

    fun fetchMotivationalMessage(apiKey: String) {
        viewModelScope.launch {
            _motivationalMessage.value = repository.fetchMotivationalMessage(apiKey)
        }
    }

    fun fetchSavedMessages() {
        viewModelScope.launch {
            _savedMessages.value = repository.getAllSavedMessages()
        }
    }
}
