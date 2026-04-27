package com.bopp.bopp.bopp.spamemail

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class EmailService {
    // one to get the emails. the other to convert from email to actual user email object

    private val mapper = ObjectMapper()
    private val client = WebClient.create()

    fun getEmails(accessToken: String): List<UserEmail> {
        val listResponse = client.get()
            .uri("https://gmail.googleapis.com/gmail/v1/users/me/messages?maxResults=10")
            .header("Authorization", "Bearer $accessToken")
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        val root = mapper.readTree(listResponse)
        val messages = root["messages"] ?: return emptyList()

        val results = mutableListOf<UserEmail>()

        for (msg in messages) {
            val id = msg["id"].asText()

            val messageResponse = client.get()
                .uri("https://gmail.googleapis.com/gmail/v1/users/me/messages/{id}?format=full", id)
                .header("Authorization", "Bearer $accessToken")
                .retrieve()
                .bodyToMono(String::class.java)
                .block()

            val message = mapper.readValue(messageResponse, GmailMessage::class.java)
            val subject = message.payload.headers
                .find { it.name == "Subject" }
                ?.value ?: "(no subject)"

            results.add(
                UserEmail(
                    id = id,
                    subject = subject,
                    body = message.payload.getReadableBody()
                )
            )
        }

        return results
    }

    fun markEmailsAsSpam(accessToken: String, emailIds: List<String>) {
        emailIds.distinct().forEach { id ->
            client.post()
                .uri("https://gmail.googleapis.com/gmail/v1/users/me/messages/{id}/modify", id)
                .header("Authorization", "Bearer $accessToken")
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
    }
}
