package com.example.nutritrack.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutritrack.model.Patient
import com.example.nutritrack.repository.PatientRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: PatientRepository) : ViewModel() {

    private val _patients = MutableStateFlow<List<Patient>>(emptyList())
    val patients: StateFlow<List<Patient>> = _patients

    fun loadPatients() {
        viewModelScope.launch {
            _patients.value = repository.getAllPatients()
        }
    }

    fun validateUserByNumber(userId: String, phoneNumber: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val patient = repository.getPatientById(userId)
            onResult(patient?.phoneNumber == phoneNumber)
        }
    }

    // In LoginViewModel
    fun validateUserByPassword(userId: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val patient = repository.getPatientById(userId)
            Log.e("passw", password + " " + patient?.password)
            if (patient != null && patient.password == password) {
                // If UserID and Password match, user is validated
                onResult(true)
            } else {
                // If UserID or Password doesn't match, invalid
                onResult(false)
            }
        }
    }


    fun getPatient(userId: String, onResult: (Patient?) -> Unit) {
        viewModelScope.launch {
            val patient = repository.getPatientById(userId)
            onResult(patient)
        }
    }

    fun getAllPatient( onResult: (List<Patient>?) -> Unit) {
        viewModelScope.launch {
            val patient = repository.getAllPatients()
            onResult(patient)
        }
    }


    fun claimAccount(userId: String, name: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val patient = repository.getPatientById(userId)
            if (patient != null && patient.name.isBlank() && patient.password.isBlank()) {
                repository.updatePatientCredentials(userId, name, password)
                onResult(true)
            } else {
                onResult(false) // already claimed or invalid
            }
        }
    }

    fun login(userId: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val patient = repository.getPatientById(userId)
            if (patient != null && patient.password == password) {
                onResult(true)
            } else {
                onResult(false)
            }
        }


    }

}
