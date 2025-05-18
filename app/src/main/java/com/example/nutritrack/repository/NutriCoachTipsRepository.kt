package com.example.nutritrack.repository

import com.example.nutritrack.dao.NutriCoachTipsDao
import com.example.nutritrack.model.NutriCoachTips
import com.example.nutritrack.request.Content
import com.example.nutritrack.request.MessageRequest
import com.example.nutritrack.request.Part
import com.example.nutritrack.service.GeminiApiService
import com.example.nutritrack.utils.Result

class NutriCoachTipsRepository(private val apiService: GeminiApiService, private val dao: NutriCoachTipsDao) {

    // Fetch motivational message from the Gemini API
    suspend fun fetchMotivationalMessage(apiKey: String): Result<String> {
        return try {
            val response = apiService.generateMotivationalMessage(apiKey, MessageRequest(
                contents = listOf(
                    Content(parts = listOf(Part(text = "Generate a short encouraging message to help someone improve their fruit intake.")))
                )
            ))

            if (response.isSuccessful) {
                // Extract message from the candidates response
                val message = response.body()?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "Error generating message"
                saveMessageToDb(message)
                Result.Success(message)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                Result.Error(Exception("Error: $errorMessage"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    // Save the generated message to the database
    private suspend fun saveMessageToDb(message: String) {
        dao.insertTip(NutriCoachTips(message = message))
    }

    // Get all saved messages from the database
    suspend fun getAllSavedMessages(): List<NutriCoachTips> {
        return dao.getAllTips()
    }
}
