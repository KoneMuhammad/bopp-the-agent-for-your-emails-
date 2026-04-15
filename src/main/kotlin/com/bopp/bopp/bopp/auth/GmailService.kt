package com.bopp.bopp.bopp.auth

import com.bopp.bopp.bopp.OAuthRepository
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class GmailService(
    private val oauthRepo: OAuthRepository
) {

    fun fetchEmails(userId: Long): List<String> {

        val token = oauthRepo.findById(userId).orElseThrow()

        val response = WebClient.create()
            .get()
            .uri("https://gmail.googleapis.com/gmail/v1/users/me/messages?maxResults=5")
            .header("Authorization", "Bearer ${token.accessToken}")
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        return listOf(response ?: "")
    }
}