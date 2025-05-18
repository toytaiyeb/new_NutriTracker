package com.example.nutritrack.service

import com.example.nutritrack.model.FruitDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface FruityViceApiService {

    // Endpoint to fetch fruit data by name
    @GET("fruit/{name}")
    suspend fun getFruitDetails(@Path("name") fruitName: String): Response<FruitDetails>
}
