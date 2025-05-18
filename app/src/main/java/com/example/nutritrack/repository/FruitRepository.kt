package com.example.nutritrack.repository

import com.example.nutritrack.model.FruitDetails
import com.example.nutritrack.service.FruityViceApiService

class FruitRepository(private val apiService: FruityViceApiService) {

    // Fetch fruit details from the API
    suspend fun getFruitDetails(fruitName: String): com.example.nutritrack.utils.Result<FruitDetails> {
        return try {
            val response = apiService.getFruitDetails(fruitName)
            if (response.isSuccessful) {
                com.example.nutritrack.utils.Result.Success(response.body())
            } else {
                com.example.nutritrack.utils.Result.Error(Exception("Error fetching data"))
            }
        } catch (e: Exception) {
            com.example.nutritrack.utils.Result.Error(e)
        }
    }
}

