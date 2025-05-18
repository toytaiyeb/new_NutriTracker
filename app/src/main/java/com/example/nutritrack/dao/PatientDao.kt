package com.example.nutritrack.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nutritrack.model.Patient

@Dao
interface PatientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatients(patients: List<Patient>)

    @Query("SELECT * FROM patients WHERE userId = :userId")
    suspend fun getPatientById(userId: String): Patient?

    @Query("SELECT * FROM patients")
    suspend fun getAllPatients(): List<Patient>

    @Query("UPDATE patients SET name = :name, password = :password WHERE userId = :userId")
    suspend fun updateNameAndPassword(userId: String, name: String, password: String)

}
