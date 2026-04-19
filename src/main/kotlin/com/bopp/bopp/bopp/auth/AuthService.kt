package com.bopp.bopp.bopp.auth

import com.infisical.sdk.models.Secret
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import tools.jackson.databind.ObjectMapper
import java.net.URLEncoder

@Service
class AuthService {

    val mapper = ObjectMapper()
    fun getToken(authCode: String, clientID: Secret): OAuthToken {

        val redirectUri = "https://bopp-backend.onrender.com"
        val body = "code=$authCode" +
                "&client_id=$clientID" +
                "&redirect_uri=${URLEncoder.encode(redirectUri, "UTF-8")}" +
                "&grant_type=authorization_code"

        val response = WebClient.builder()
            .baseUrl("https://oauth2.googleapis.com/token")
            .defaultHeader("Content-Type", "application/x-www-form-urlencoded")
            .build()
            .post()
            .bodyValue(body)
            .retrieve()
            .bodyToMono(OAuthToken::class.java)
            .block()


        return response!!
    }

}