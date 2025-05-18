package com.example.nutritrack.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "food_intake",
    foreignKeys = [
        ForeignKey(
            entity = Patient::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FoodIntake(
    @PrimaryKey @ColumnInfo(name = "userId") val userId: String, // Use userId as the primary key
    val selectedCategories: String, // Comma-separated selected food categories
    val selectedPersona: String,    // Selected persona
    val biggestMealTime: String,   // Time of biggest meal
    val sleepTime: String,         // Sleep time
    val wakeTime: String           // Wake time
)