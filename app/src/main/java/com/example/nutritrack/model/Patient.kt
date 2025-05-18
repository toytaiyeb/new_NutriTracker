package com.example.nutritrack.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patients")
data class Patient(
    @PrimaryKey val userId: String,
    val phoneNumber: String,
    val name: String="",
    val password: String="",
    val sex: String,
    val heifaTotalScore: Float,
    val vegetablesScore: Float,
    val fruitScore: Float,
    val grainsCerealScore: Float,
    val wholeGrainsScore: Float,
    val meatAlternativesScore: Float,
    val dairyAlternativesScore: Float,
    val waterScore: Float,
    val unsaturatedFatScore: Float,
    val sodiumScore: Float,
    val sugarScore: Float,
    val alcoholScore: Float,
    val discretionaryScore: Float
)
