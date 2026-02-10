package com.bopp.bopp.bopp

import org.springframework.stereotype.Component

@Component
class WebClientConfig(private val aiConfig: AIConfig) {

    private val webClient = WebClient.builder()
        .baseUrl(aiConfig.baseUrl)
        .defaultHeader("Authorization", "Bearer ${aiConfig.apiKey}")
        .defaultHeader("Content-Type", "application/json")
        .build()

    suspend fun chat(userMessage: String): String {
        val request = ChatRequest(
            model = aiConfig.model,
            messages = listOf(ChatMessage("user", userMessage))
        )

        val response: ChatResponse = webClient.post()
            .uri("/chat/completions")
            .bodyValue(request)
            .retrieve()
            .awaitBody()

        return response.choices.first().message.content
    }
}