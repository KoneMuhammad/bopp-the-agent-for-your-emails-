package com.bopp.bopp.bopp.service

import com.bopp.bopp.bopp.DTO.EmailDTO
import com.bopp.bopp.bopp.DTO.LLMDecision
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class LLMService(
    private val webClient: WebClient = WebClient.create()
) {

    fun classifyBatch(emails: List<EmailDTO>): List<LLMDecision> {

        val prompt = buildPrompt(emails)

        val response = webClient.post()
            .uri("https://api.openai.com/v1/chat/completions")
            .header("Authorization", "Bearer YOUR_API_KEY")
            .bodyValue(mapOf(
                "model" to "gpt-4o-mini",
                "messages" to listOf(
                    mapOf("role" to "user", "content" to prompt)
                )
            ))
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        // TODO: parse JSON properly
        return parseResponse(response!!)
    }

    private fun buildPrompt(emails: List<EmailDTO>): String {
        return """
        Classify emails into spam, important, promotions.
        Return JSON only.

        Emails:
        ${emails}
        """.trimIndent()
    }

    private fun parseResponse(response: String): List<LLMDecision> {
        // stub
        return emptyList()
    }
}

