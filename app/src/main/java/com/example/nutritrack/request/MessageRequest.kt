package com.example.nutritrack.request

// Request model to send to the Gemini API
data class MessageRequest(
    val contents: List<Content>
)

data class Content(
    val parts: List<Part>
)

data class Part(
    val text: String
)


