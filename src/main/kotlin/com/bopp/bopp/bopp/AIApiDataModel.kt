package com.bopp.bopp.bopp


data class ChatMessage(
    val role: String,
    val content: String
)

data class ChatRequest(
    val model: String,
    val messages: List<ChatMessage>
)

data class ChatResponse(
    val choices: List<Choice>
) {
    data class Choice(
        val message: ChatMessage
    )
}