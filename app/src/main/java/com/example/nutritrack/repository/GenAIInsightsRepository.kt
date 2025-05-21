package com.example.nutritrack.repository

import com.example.nutritrack.dao.PatientDao
import com.example.nutritrack.model.Patient
import com.example.nutritrack.request.Content
import com.example.nutritrack.request.MessageRequest
import com.example.nutritrack.request.Part
import com.example.nutritrack.service.GeminiApiService
import com.example.nutritrack.utils.Result

class GenAIInsightsRepository(
    private val apiService: GeminiApiService,
    private val patientDao: PatientDao
) {

    suspend fun fetchInsightsFromPatientData(apiKey: String): Result<List<String>> {
        return try {
            val patients = patientDao.getAllPatients()
            val csvData = convertPatientsToCSV(patients)

            val prompt = """
                Based on the following nutrition data of patients, identify 3 interesting trends or patterns. 
                This can include things like gender differences in scores, relationships between different score types, 
                or unusual outliers. Return exactly 3 short and clear insights in bullet points or numbered format.

                $csvData
            """.trimIndent()

            val response = apiService.generateInsights(
                apiKey,
                MessageRequest(
                    contents = listOf(Content(parts = listOf(Part(text = prompt))))
                )
            )

            if (response.isSuccessful) {
                val text = response.body()?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                val insights = text?.split("\n")?.filter { it.isNotBlank() } ?: listOf("No insights generated")
                Result.Success(insights)
            } else {
                val error = response.errorBody()?.string() ?: "Unknown error"
                Result.Error(Exception("Error from API: $error"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    private fun convertPatientsToCSV(patients: List<Patient>): String {
        val header = listOf(
            "UserID", "Sex", "HEIFA_Total", "Vegetables", "Fruit", "Grains&Cereals", "WholeGrains",
            "MeatAlternatives", "DairyAlternatives", "Water", "UnsaturatedFat", "Sodium", "Sugar",
            "Alcohol", "Discretionary"
        ).joinToString(",")

        val rows = patients.map {
            listOf(
                it.userId,
                it.sex,
                it.heifaTotalScore,
                it.vegetablesScore,
                it.fruitScore,
                it.grainsCerealScore,
                it.wholeGrainsScore,
                it.meatAlternativesScore,
                it.dairyAlternativesScore,
                it.waterScore,
                it.unsaturatedFatScore,
                it.sodiumScore,
                it.sugarScore,
                it.alcoholScore,
                it.discretionaryScore
            ).joinToString(",")
        }

        return (listOf(header) + rows).joinToString("\n")
    }
}
