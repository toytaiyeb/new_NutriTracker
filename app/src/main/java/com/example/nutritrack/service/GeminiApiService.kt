package com.example.nutritrack.service

import com.example.nutritrack.request.MessageRequest
import com.example.nutritrack.response.MessageResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GeminiApiService {

    @POST("v1beta/models/gemini-2.0-flash:generateContent")
    suspend fun generateInsights(
        @Query("key") apiKey: String, // The API key as query parameter
        @Body requestBody: MessageRequest
    ): Response<MessageResponse>
}
