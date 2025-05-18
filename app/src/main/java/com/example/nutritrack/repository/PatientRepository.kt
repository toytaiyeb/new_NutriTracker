package com.example.nutritrack.repository

import com.example.nutritrack.dao.PatientDao
import com.example.nutritrack.model.Patient

class PatientRepository(private val patientDao: PatientDao) {

    suspend fun insertPatients(patients: List<Patient>) {
        patientDao.insertPatients(patients)
    }

    suspend fun getPatientById(userId: String): Patient? {
        return patientDao.getPatientById(userId)
    }

    suspend fun getAllPatients(): List<Patient> {
        return patientDao.getAllPatients()
    }

    suspend fun updatePatientCredentials(userId: String, name: String, password: String) {
        patientDao.updateNameAndPassword(userId, name, password)
    }

}
