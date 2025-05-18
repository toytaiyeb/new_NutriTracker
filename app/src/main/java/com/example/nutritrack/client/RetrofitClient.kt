package com.example.nutritrack.client

import com.example.nutritrack.service.FruityViceApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://www.fruityvice.com/api/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: FruityViceApiService by lazy {
        retrofit.create(FruityViceApiService::class.java)
    }
}
