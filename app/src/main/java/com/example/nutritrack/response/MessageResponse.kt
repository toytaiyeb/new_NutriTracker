package com.example.nutritrack.response


data class MessageResponse(
    val candidates: List<Candidate>,
    val usageMetadata: UsageMetadata,
    val modelVersion: String
)

data class Candidate(
    val content: Content,
    val finishReason: String,
    val avgLogprobs: Double
)

data class Content(
    val parts: List<Part>
)

data class Part(
    val text: String
)

data class UsageMetadata(
    val promptTokenCount: Int,
    val candidatesTokenCount: Int,
    val totalTokenCount: Int,
    val promptTokensDetails: List<TokenDetail>,
    val candidatesTokensDetails: List<TokenDetail>
)

data class TokenDetail(
    val modality: String,
    val tokenCount: Int
)
