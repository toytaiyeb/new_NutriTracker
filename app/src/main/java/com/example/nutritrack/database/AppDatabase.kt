package com.example.nutritrack.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nutritrack.dao.NutriCoachTipsDao
import com.example.nutritrack.dao.FoodIntakeDao
import com.example.nutritrack.dao.PatientDao
import com.example.nutritrack.model.NutriCoachTips
import com.example.nutritrack.model.FoodIntake
import com.example.nutritrack.model.Patient

@Database(entities = [Patient::class, FoodIntake::class, NutriCoachTips::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun patientDao(): PatientDao
    abstract fun foodIntakeDao(): FoodIntakeDao
    abstract fun nutriCoachTipsDao(): NutriCoachTipsDao
}
