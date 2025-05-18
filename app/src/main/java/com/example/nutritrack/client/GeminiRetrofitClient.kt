package com.example.nutritrack.client

import android.content.ContentValues.TAG
import android.util.Log
import com.example.nutritrack.service.GeminiApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object GeminiRetrofitClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val okHttpClient: OkHttpClient by lazy {
        provideOkHttpClient()
    }

    private fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                try {
                    val response = chain.proceed(request)
                    if (!response.isSuccessful) {
                        Log.e(TAG, "API Error: ${response.code} - ${response.message}")
                    }
                    response
                } catch (e: IOException) {
                    Log.e(TAG, "Network Error: ${e.message}", e)
                    throw e
                }
            }
            .connectTimeout(30, TimeUnit.SECONDS) // Set connection timeout
            .readTimeout(30, TimeUnit.SECONDS)    // Set read timeout
            .writeTimeout(30, TimeUnit.SECONDS)   // Set write timeout
            .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: GeminiApiService by lazy {
        retrofit.create(GeminiApiService::class.java)
    }
}