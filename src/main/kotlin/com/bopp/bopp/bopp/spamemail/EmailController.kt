package com.bopp.bopp.bopp.spamemail

import com.bopp.bopp.bopp.ai.AIService
import kotlinx.coroutines.flow.flowOf
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient

@RestController
class EmailController(
    val aIService: AIService
) {


    @PostMapping("/email/spam")
    fun setEmailsSpam(@RequestBody emails: List<UserEmail>) {
        val webClient = WebClient.builder().build()

        emails.forEach { email ->
            webClient.post()
                .uri("https://gmail.googleapis.com/gmail/v1/users/me/messages/{id}/modify", email.id)
                .header("Authorization", "Bearer TOKEN")
                .bodyValue(
                    mapOf(
                        "addLabelIds" to listOf("SPAM"),
                        "removeLabelIds" to listOf("INBOX")
                    )
                )
                .retrieve()
                .bodyToMono(String::class.java)
                .block()
        }

        aIService.getSpamEmailDecision(emails)

    }
}




