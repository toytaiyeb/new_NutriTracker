package com.example.nutritrack.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nutri_coach_tips")
data class NutriCoachTips(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val message: String
)
