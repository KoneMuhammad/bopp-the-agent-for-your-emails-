package com.bopp.bopp.bopp.ai

import com.bopp.bopp.bopp.security.SecretService
import com.bopp.bopp.bopp.spamemail.UserEmail
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class AIService(
    private val secretService: SecretService,
    private val objectMapper: ObjectMapper,
) {

    private val webClient = WebClient.builder()
        .baseUrl("https://api.deepseek.com/chat/completions")
        .build()

    fun getSpamEmailDecision(emails: List<UserEmail>): List<String> {
        if (emails.isEmpty()) {
            return emptyList()
        }

        val emailDetails = emails.joinToString(separator = "\n") { email ->
            "ID: ${email.id}, Subject: ${email.subject}, Body: ${email.body}"
        }

        val response = webClient.post()
            .header("Authorization", "Bearer ${secretService.getApiKey().secretValue}")
            .header("Content-Type", "application/json")
            .bodyValue(
                mapOf(
                    "model" to "deepseek-v4-pro",
                    "messages" to listOf(
                        mapOf(
                            "role" to "system",
                            "content" to """
                                You determine which emails are spam and which ones are not.
                                Return only the spam email IDs, one per line, with no extra text.
                                Here are the emails for classification:
                                $emailDetails
                            """.trimIndent()
                        ),
                        mapOf(
                            "role" to "user",
                            "content" to "List only the IDs for emails that should be marked as spam."
                        )
                    )
                )
            )
            .retrieve()
            .bodyToMono(String::class.java)
            .block() ?: return emptyList()

        val content = objectMapper.readTree(response)
            .path("choices")
            .elements()
            .asSequence()
            .firstOrNull()
            ?.path("message")
            ?.path("content")
            ?.asText()
            ?: return emptyList()

        return content.lines()
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .map { it.removePrefix("ID:").trim() }
            .filter { candidate -> emails.any { it.id == candidate } }
    }
}
