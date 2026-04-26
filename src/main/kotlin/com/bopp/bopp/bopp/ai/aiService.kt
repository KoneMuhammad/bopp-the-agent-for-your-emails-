package com.bopp.bopp.bopp.ai

import com.bopp.bopp.bopp.spamemail.UserEmail
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class AIService {

    fun getSpamEmailDecision(emails: List<UserEmail>): String {
        val webClient = WebClient.builder().build()

        val emailDetails = emails.joinToString(separator = "\n") { email ->
            "ID: ${email.id}, Subject: ${email.subject}, Body: ${email.body}"
        }

        val response = webClient.post()
            .uri("https://api.deepseek.com/chat/completions")
            .header("Authorization", "Bearer YOUR_TOKEN")
            .header("Content-Type", "application/json")
            .bodyValue(
                mapOf(
                    "model" to "deepseek-v4-pro",
                    "messages" to listOf(
                        mapOf(
                            "role" to "system",
                            "content" to "You determine which emails are spam and which ones are not. Respond in the format: '<email_id> = spam' or '<email_id> = not spam'. Here are the emails for classification:\n$emailDetails"
                        ),
                        mapOf("role" to "user", "content" to "Please classify the emails listed above.")
                    )
                )
            )
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        return response!!
    }
}
